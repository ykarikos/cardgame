(ns cardgame.network.client
    (:require [cardgame.network.server :as server]
              [cardgame.state :as state]
              [manifold.stream :as s]
              [aleph.http :as http]
              [clojure.data.json :as json]))

(def connection (atom nil))

(defn- execute [command params]
  (println (:msg params))
  (case command
      "quit" (s/close! @connection)
      "state" (state/change-state (:state params))))

(defn- parse-json [s]
    (try
        (json/read-str s :key-fn keyword)
        (catch Exception e {})))

(defn- consumer [data]
  (let [json-data (parse-json data)
        command (:command json-data)
        params (:params json-data)]
    (execute command params)))

(defn send-message [command params]
  (when-not (s/closed? @@connection)
    (server/send-message @@connection command params)))

(defn connect [hostname username]
    (do
        (reset! connection (http/websocket-client (str "ws://" hostname ":" server/port)))
        (s/consume consumer @@connection)
        (send-message "join" username)))
