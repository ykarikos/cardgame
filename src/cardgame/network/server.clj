(ns cardgame.network.server
    (:require [cardgame.commands.remote :as cmd]
              [clojure.data.json :as json]
              [manifold.stream :as s]
              [aleph.http :as http]))

(def port 9876)

(def server (atom nil))
(def server-out (s/stream))

(declare send-message)

(defn- execute [command params]
    (case command
        "join" (send-message (cmd/join params))))

(defn- parse-json [s]
    (try
        (json/read-str s :key-fn keyword)
        (catch Exception e {})))

(defn- consumer [data]
  (println "received" data)
  (let [json-data (parse-json data)
        command (:command json-data)
        params (:params json-data)]
    (execute command params)))

(defn- handler [req]
  (let [net @(http/websocket-connection req)]
    (s/connect server-out net)
    (s/consume consumer net)))

(defn started? []
  (not (= nil @server)))

(defn start-server []
    (if (started?)
        (println "Server already started")
        (do
          (reset! server (http/start-server handler {:port port}))
          (println "Server started."))))

(defn- close! [server]
  (.close server))

(defn terminate-server []
    (when (started?)
        (swap! server close!)))

(defn send-message
  ([data]
    (send-message (:command data) data))
  ([command params]
    (when (started?)
      (send-message server-out command params)))
  ([connection command params]
    (s/put! connection (json/write-str {:command command :params params}))))
