(ns cardgame.commands
    "Commands that can be issued in the prompt. The first parameter of each command is the current game state. Each command should return a message containing e.g. the changes in game state in the key :new-state."
    (:require [cardgame.decks.core :as decks]))

(defn create-game [state name number-of-players decktype]
    (let [deck (decks/get-deck decktype)]
        (when deck
            {:new-state
                {:name name
                 :number-of-players number-of-players
                 :deck (shuffle deck)}})))

(defn quit [state]
    nil)

(defn execute [command params]
    "Execute given command if it is found in local namespace with a correct arity. Return the new prompt-prefix."
    (let [command-fun (resolve (symbol "cardgame.commands" command))
          arities (->> command-fun meta :arglists (map count))]
        (if-not command-fun
            (println (str "Command '" command "' not found."))
            (if (= -1 (.indexOf arities (count params)))
                (println (str "Wrong number of arguments for '" command "'."))
                (apply command-fun params)))))

