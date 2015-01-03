(ns cardgame.core
    (:gen-class)
    (:require [cardgame.prompt :as prompt]
              [cardgame.state :as state]))

(def usage
    "java -jar cardgame.jar [username]")

(defn -main
  [& args]
  (if (< (count args) 1)
    (println usage)
    (prompt/prompt (state/change-state {:username (first args)}))))
