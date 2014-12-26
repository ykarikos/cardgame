(ns cardgame.commands
    "Commands that can be issued in the prompt"
    (:require [cardgame.decks.core :as decks]))


(defn create-game [name number-of-players decktype]
    (let [deck (decks/get-deck decktype)]
        (when deck
            {:name name
             :number-of-players number-of-players
             :deck (shuffle deck)})))