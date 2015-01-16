(ns cardgame.state)

(def state (atom {}))

(defn change-state [new-state]
    (swap! state merge new-state))

(defn game-full?
    ([] (game-full? @state))
    ([state]
        (let [playercount (:playercount state)
              joined (:joined state)]
            (and joined playercount (= joined playercount)))))

(defn join-player [username]
    (change-state
      {:joined (+ 1 (:joined @state))
       :players (cons username (:players @state))}))
