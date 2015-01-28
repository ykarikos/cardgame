(ns cardgame.network.client
    (:require [cardgame.network.core :as net]
              [cardgame.state :as state]
              [manifold.stream :as s]
              [aleph.http :as http]
              [clojure.data.json :as json]))

(def connection (atom nil))

(defn closed? []
  (or (= @connection nil)
    (s/closed? @@connection)))

(defn send-message [data]
  (when-not (closed?)
    (net/send-message @@connection data)))

(defn close-connection []
  (when-not (closed?)
    (reset! connection (s/close! @@connection))))

(defn- execute [data]
  (let [params (:params data)]
    (case (:command data)
      "quit" (close-connection)
      "state" (do (println "received" params) (state/change-state params)))))

(defn connect [hostname username]
    (do
        (reset! connection (http/websocket-client (str "ws://" hostname ":" net/port)))
        (s/consume (net/get-consumer execute) @@connection)
        (send-message {:command "join"
                       :msg (str username " joins the game.")
                       :params username})))
