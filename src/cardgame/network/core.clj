(ns cardgame.network.core
    "The network protocol format:
{:command \"state\"
 :msg \"Joined.\"}
 :params {:game {:playercount 2
          :joined 1
          :players (\"John\")
          :deck (\"A♠8♡6♢K♣\")}
"
    (:require [manifold.stream :as s]
              [cardgame.state :as state]
              [aleph.http :as http]
              [clojure.data.json :as json]))


(def port 9876)

(defn parse-json [s]
    (try
        (json/read-str s :key-fn keyword)
        (catch Exception e {})))

(defn- print-msg [message]
    (when message
        (println (str "\n" message))
        (state/print-prompt)))

(defn get-consumer [executor]
    (fn [data]
;        (println "received" data)
        (let [json-data (parse-json data)]
            (print-msg (:msg json-data))
            (executor json-data))))

(defn send-message [connection data]
    (s/put! connection (json/write-str data)))
