(ns cardgame.network.client
    (:require [cardgame.network.core :as net]
              [cardgame.state :as state]
              [manifold.stream :as s]
              [aleph.http :as http]
              [clojure.data.json :as json]))

(def connection (atom nil))

(defn- execute [data]
  (let [params (:params data)]
    (case (:command data)
      "quit" (s/close! @@connection)
      "state" (state/change-game-state params))))

(defn send-message [data]
  (when-not (s/closed? @@connection)
    (net/send-message @@connection data)))

(defn connect [hostname username]
    (do
        (reset! connection (http/websocket-client (str "ws://" hostname ":" net/port)))
        (s/consume (net/get-consumer execute) @@connection)
        (send-message {:command "join"
                       :msg (str username " joins the game.")
                       :params username})))
