(ns cardgame.state
    "The state format:
{:local {:username \"John\"}
 :game {:name \"evening bridge\"
        :playercount 2
        :joined 1
        :players (\"John\")
        :deck [\"A♠8♡6♢K♣\"]}
}")

(def prompt-symbol "> ")

(def state (atom {}))

(defn initial-state [username]
    (swap! state merge
      {:local
        {:username username}}))

(defn change-state [new-state]
    (swap! state merge new-state))

(defn change-game-state [new-game-state]
    (swap! state merge {:game new-game-state}))

(defn game-full?
    ([] (game-full? @state))
    ([state]
        (let [playercount (-> state :game :playercount)
              joined (-> state :game :joined)]
            (and joined playercount (= joined playercount)))))

(defn join-player [username]
    (let [new-state
            {:game (merge (:game @state)
                {:joined (+ 1 (-> @state :game :joined))
                 :players (cons username (-> @state :game :players))})}]
        (:game (swap! state merge new-state))))

(defn print-prompt []
    (print (str (-> @state :game :name) prompt-symbol))
    (flush))