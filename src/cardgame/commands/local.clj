(ns cardgame.commands.local
    "Commands that can be issued in the prompt. The first parameter of each command is the current game state. Each command should return the new state."
    (:require [cardgame.decks.core :as decks]
              [cardgame.state :as state]
              [cardgame.network.server :as server]
              [cardgame.network.client :as client]))

; TODO
; (def usage "")

(defn create-game [state gamename players decktype]
    (let [deck (decks/get-deck decktype)]
        (when (and (not (server/started?)) deck)
            (server/start-server)
            {:name gamename
             :prompt-prefix (str gamename "@local")
             :playercount (read-string players)
             :players (list (:username state)) ; maybe username@host ?
             :joined 1
             :deck (shuffle deck)})))

(defn- get-rest [v n]
    (if (> n (count v))
        []
        (subvec v n)))

; TODO: select cards for each player one-by-one
(defn deal [state cardcount]
    (if-not (state/game-full? state)
        (println "Waiting for more players. Can't deal cards yet.")
        (let [number-of-cards-to-deal (* (read-string cardcount) (:playercount state))
              dealt-cards (take number-of-cards-to-deal (:deck state))
              rest-deck (get-rest (:deck state) number-of-cards-to-deal)]
            (println "Deal " dealt-cards)
            {:deck rest-deck})))

(defn join [state hostname]
    (if (server/started?)
        (println "Can not join a game when a local server is started.")
        (do (client/connect hostname (:username state)) {})))

(defn quit [state]
    (when (server/started?)
        (server/terminate-server)))

(defn execute [command params]
    "Execute given command if it is found in local namespace with a correct arity. Return the new state."
    (let [command-fun (resolve (symbol "cardgame.commands.local" command))
          arities (->> command-fun meta :arglists (map count))]
        (if-not command-fun
            (println (str "Command '" command "' not found."))
            (if (= -1 (.indexOf arities (count params)))
                (println (str "Wrong number of arguments for '" command "'."))
                (apply command-fun params)))))

