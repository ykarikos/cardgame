(ns cardgame.network.server
    (:require [cardgame.commands.remote :as cmd]
              [cardgame.network.core :as net]
              [cardgame.state :as state]
              [clojure.data.json :as json]
              [manifold.stream :as s]
              [aleph.http :as http]))

(def server (atom nil))
(def server-out (s/stream))

(declare send-message)

(defn- execute [data]
  (let [params (:params data)]
    (case (:command data)
        "join" (send-message (cmd/join params))
        "state" (state/change-state params))))

(defn- handler [req]
  (let [net @(http/websocket-connection req)]
    (s/connect server-out net)
    (s/consume (net/get-consumer execute) net)))

(defn started? []
  (not (= nil @server)))

(defn start-server []
    (if (started?)
        (println "Server already started")
        (do
          (reset! server (http/start-server handler {:port net/port}))
          (println "Server started."))))

(defn- close! [server]
  (.close server))

(defn terminate-server []
    (when (started?)
        (swap! server close!)))

(defn send-message [data]
  (when (started?)
    (net/send-message server-out data)))