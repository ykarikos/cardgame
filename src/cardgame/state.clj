(ns cardgame.state
    "The state format:
{:local {:username \"John\"
         :hand [\"♠2\" \"♥Q\"]}
 :game {:name \"evening-bridge\"
        :playercount 2
        :joined 1
        :players (\"John\")
        :deck [\"♠A\" \"♥9\" \"♦6\" \"♣K\"]}
}")

(def prompt-symbol "> ")

(def state (atom {}))

(defn initial-state [username]
    (swap! state merge
      {:local
        {:hand []
         :username username}}))

(defn- merge-state [state new-state]
    (let [new-hand (-> new-state :local :hand)
          new-game (:game new-state)
          combined-hand (into (-> state :local :hand) new-hand)] ; TODO sort hand
        {:local (merge (:local state) {:hand combined-hand})
         :game (merge (:game state) new-game)}))

(defn change-state [new-state]
  (println "new state" new-state)
  (swap! state merge-state new-state))

(defn game-full? []
    (let [playercount (-> @state :game :playercount)
          joined (-> @state :game :joined)]
        (and joined playercount (= joined playercount))))

(defn join-player [username]
    (let [new-state
            {:game {:joined (+ 1 (-> @state :game :joined))
                    :players (cons username (-> @state :game :players))}}]
        (dissoc (change-state new-state) :local)))

(defn print-prompt []
    (print (str (-> @state :game :name) prompt-symbol))
    (flush))

(defn print-msg [message]
    (when message
        (println (str "\n" message))
        (print-prompt)))

