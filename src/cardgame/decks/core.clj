(ns cardgame.decks.core
    "Access to all different decks"
    (:require [cardgame.decks.french :as french]))

(defn get-deck [name]
    (case name
        "french" (french/get-deck)
        nil))
