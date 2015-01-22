(ns cardgame.decks.french-test
  (:require [clojure.test :refer :all]
            [cardgame.decks.french :refer :all]))

(defn- count-value-sum [deck]
    (->> deck (map numeral-value) (reduce +)))

(deftest french-deck
  (testing "foo"
    (let [deck (get-deck)
          values (count-value-sum deck)
          shuffled-values (count-value-sum (shuffle deck))]
        (is (= (count (shuffle deck)) (count deck) 52))
        (is (= values shuffled-values 364))

        (is (is-spade? (first deck)))
        (is (= 1 (numeral-value (first deck))))
        (is (not (is-diamonds? (first deck))))
        (is (not (is-clubs? (first deck))))
        (is (not (is-hearts? (first deck))))

        (is (is-clubs? (last deck)))
        (is (= 13 (numeral-value (last deck))))
        (is (not (is-diamonds? (last deck))))
        (is (not (is-spade? (last deck))))
        (is (not (is-hearts? (last deck)))))))

