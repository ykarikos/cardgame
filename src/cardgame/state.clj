(ns cardgame.state)

(def state (atom {}))

(defn change-state [new-state]
    (swap! state merge new-state))
