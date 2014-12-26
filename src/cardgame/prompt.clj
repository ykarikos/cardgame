(ns cardgame.prompt
    (:require [cardgame.commands :as cmd]))

(def prompt-symbol "> ")

(defn prompt [username]
    (loop [prompt-prefix ""]
        (print (str prompt-prefix prompt-symbol))
        (flush)
        (let [new-prefix (cmd/execute prompt-prefix username (read-line))]
            (when new-prefix
                (recur new-prefix)))))

