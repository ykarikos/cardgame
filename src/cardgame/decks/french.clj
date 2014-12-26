(ns cardgame.decks.french
  "The french deck")

(def values "A23456789TJQK")
(def suits "♠♡♢♣")

(defn- cart [colls]
  (if (empty? colls)
    '(())
    (for [x (first colls)
          more (cart (rest colls))]
      (cons x more))))

(defn is-spade? [card]
    (= \♠ (first card)))

(defn is-diamonds? [card]
    (= \♢ (first card)))

(defn is-clubs? [card]
    (= \♣ (first card)))

(defn is-hearts? [card]
    (= \♡ (first card)))

(defn numeral-value [card]
    (let [val (subs card 1)]
        (case val
            "A" 1
            "T" 10
            "J" 11
            "Q" 12
            "K" 13
            (read-string val))))

(defn get-deck []
    (map #(reduce str %) (cart (list suits values))))

