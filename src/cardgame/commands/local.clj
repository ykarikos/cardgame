(ns cardgame.commands.local
    "Commands that can be issued in the prompt. The first parameter of each command is the current game state."
    (:require [cardgame.decks.core :as decks]
              [cardgame.state :as state]
              [cardgame.network.server :as server]
              [cardgame.network.client :as client]
              [cardgame.commands.remote :as remote]
              [clojure.string :as string]))

(defn help [state]
  (println "Commands:
- create-game gamename playercount decktype
- join hostname
- deal cardcount
- quit
- help

Supported deck types: french"))

(defn- message
  ([command params]
    {:command command
     :params params})
  ([command msg params]
    {:command command
     :msg msg
     :params params}))

(defn create-game [state gamename players decktype]
    (let [deck (decks/get-deck decktype)]
        (when (and (not (server/started?)) deck)
            (server/start-server)
            (state/change-state
                {:game
                  {:name gamename
                   :playercount (read-string players)
                   :players (list (-> state :local :username))
                   :joined 1
                   :deck (shuffle deck)}}))))


(defn- send-message [data]
  (if (server/started?)
    (server/send-message data)
    (when-not (client/closed?)
      (client/send-message data))))

(defn- deal-remote [deck username]
  (fn [cards]
    (send-message (message "state"
                           (str username " dealt you " (string/join ", " cards))
                           {:game {:deck deck}
                            :local {:hand cards}}))))

(defn deal [state cardcount-param]
  (let [username (-> state :local :username)
        cardcount (read-string cardcount-param)]
    (if-not (state/game-full?)
      (println "Waiting for more players. Can't deal cards yet.")
      (if (server/started?)
        (send-message (remote/deal username cardcount))
        (send-message (message "deal" {:username username :cardcount cardcount}))))))

(defn join [state hostname]
    (if (server/started?)
        (println "Can not join a game when a local server is started.")
        (client/connect hostname (-> state :local :username))))

(defn quit [state]
    (server/terminate-server)
    (client/close-connection))

(defn execute [command params]
    "Execute given command if it is found in local namespace with a correct arity."
    (let [command-fun (resolve (symbol "cardgame.commands.local" command))
          arities (->> command-fun meta :arglists (map count))]
        (if-not command-fun
            (println (str "Command '" command "' not found."))
            (if (= -1 (.indexOf arities (count params)))
                (println (str "Wrong number of arguments for '" command "'."))
                (apply command-fun params)))))

