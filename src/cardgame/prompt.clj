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
          params (cons @state/state (rest parts))]
        (when-not (empty? command)
          (cmd/execute command params))
        (when-not (= "quit" command)
            (recur))))

