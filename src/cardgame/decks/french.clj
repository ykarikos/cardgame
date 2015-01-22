(ns cardgame.decks.french
  "The french deck")

(def values "A23456789TJQK")
(def suits "♠♥♦♣")

(defn- cart [colls]
  (if (empty? colls)
    '(())
    (for [x (first colls)
          more (cart (rest colls))]
      (cons x more))))

(defn is-spade? [card]
    (= \♠ (second card)))

(defn is-diamonds? [card]
    (= \♦ (second card)))

(defn is-clubs? [card]
    (= \♣ (second card)))

(defn is-hearts? [card]
    (= \♥ (second card)))

(defn numeral-value [card]
    (let [val (subs card 0 1)]
        (case val
            "A" 1
            "T" 10
            "J" 11
            "Q" 12
            "K" 13
            (read-string val))))

(defn get-deck []
    (map #(reduce str %) (cart (list values suits))))

