(ns cardgame.prompt
    (:require [cardgame.commands :as cmd]
              [cardgame.state :as state]))

(def prompt-symbol "> ")

(defn- parse-command [command]
    (if-not command
        ["quit"]
        (clojure.string/split command #" ")))

(defn prompt [state]
    (println state) ; TODO: remove
    (print (str (:prompt-prefix state) prompt-symbol))
    (flush)
    (let [parts (parse-command (read-line))
          command (first parts)
          params (cons state (rest parts))
          message (cmd/execute command params)]
        (when-not (= "quit" command)
            (recur (state/change-state (:new-state message))))))

