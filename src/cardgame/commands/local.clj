(ns cardgame.commands.local
    "Commands that can be issued in the prompt. The first parameter of each command is the current game state. Each command should return the new state."
    (:require [cardgame.decks.core :as decks]
              [cardgame.state :as state]
              [cardgame.network.server :as server]
              [cardgame.network.client :as client]
              [clojure.string :as string]))

; TODO
(defn help [state]
  (println "Commands:
- create-game gamename playercount decktype
- join hostname
- deal cardcount
- quit
- help

Supported deck types: french")
  {})

(defn create-game [state gamename players decktype]
    (let [deck (decks/get-deck decktype)]
        (when (and (not (server/started?)) deck)
            (server/start-server)
            {:game
              {:name gamename
               :playercount (read-string players)
               :players (list (-> state :local :username)) ; maybe username@host ?
               :joined 1
               :deck (shuffle deck)}})))

(defn- get-rest [v n]
    (if (> n (count v))
        []
        (subvec v n)))

(defn- take-cards [deck cardcount playercount]
  (fn [skip]
    (take cardcount (take-nth playercount (get-rest deck skip)))))

(defn- send-message [data]
  (if (server/started?)
    (server/send-message data)
    (when-not (client/closed?)
      (client/send-message data))))

(defn- deal-remote [deck username]
  (fn [cards]
    (send-message {:command "state"
                   :msg (str username " dealt you " (string/join ", " cards))
                   :params {:game {:deck deck}
                            :local {:hand cards}}})))

(defn deal [state cardcount-param]
    (if-not (state/game-full? state)
        (println "Waiting for more players. Can't deal cards yet.")
        (let [deck (-> state :game :deck)
              playercount (-> state :game :playercount)
              cardcount (read-string cardcount-param)
              number-of-cards-to-deal (* cardcount playercount)
              dealt-cards (map (take-cards deck cardcount playercount) (range playercount))
              rest-deck (get-rest deck number-of-cards-to-deal)]
            (dorun (map (deal-remote rest-deck (-> state :local :username)) (take (- playercount 1) dealt-cards)))
            (state/print-msg (str "You were dealt " (string/join ", " (last dealt-cards))))
            {:local
              {:hand (into (-> state :local :hand) (last dealt-cards))}
             :game
              {:deck rest-deck}})))

(defn join [state hostname]
    (if (server/started?)
        (println "Can not join a game when a local server is started.")
        (do (client/connect hostname (-> state :local :username)) {})))

(defn quit [state]
    (server/terminate-server)
    (client/close-connection))

(defn execute [command params]
    "Execute given command if it is found in local namespace with a correct arity. Return the new state."
    (let [command-fun (resolve (symbol "cardgame.commands.local" command))
          arities (->> command-fun meta :arglists (map count))]
        (if-not command-fun
            (println (str "Command '" command "' not found."))
            (if (= -1 (.indexOf arities (count params)))
                (println (str "Wrong number of arguments for '" command "'."))
                (apply command-fun params)))))

