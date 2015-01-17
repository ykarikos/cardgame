(ns cardgame.prompt
    (:require [cardgame.commands.local :as cmd]
              [cardgame.state :as state]))

(defn- parse-command [command]
    (if-not command
        ["quit"]
        (clojure.string/split command #" ")))

(defn prompt [state]
    (println state) ; TODO: remove
    (state/print-prompt)
    (let [parts (parse-command (read-line))
          command (first parts)
          params (cons state (rest parts))
          new-state (if (empty? command) state (cmd/execute command params))]
        (when-not (= "quit" command)
            (recur (state/change-state new-state)))))

