(ns cardgame.prompt
    (:require [cardgame.commands.local :as cmd]
              [cardgame.state :as state]))

(defn- parse-command [command]
    (if-not command
        ["quit"]
        (clojure.string/split command #" ")))

(defn prompt []
    (println @state/state) ; TODO: remove
    (state/print-prompt)
    (let [parts (parse-command (read-line))
          command (first parts)
          params (cons @state/state (rest parts))
          new-state (if (empty? command) {} (cmd/execute command params))]
        (when-not (= "quit" command)
            (state/change-state new-state)
            (recur))))

