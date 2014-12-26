(ns cardgame.commands
    "Commands that can be issued in the prompt"
    (:require [cardgame.decks.core :as decks]))

(defn create-game [name number-of-players decktype]
    (let [deck (decks/get-deck decktype)]
        (when deck
            {:name name
             :number-of-players number-of-players
             :deck (shuffle deck)})))

(defn execute [prompt-prefix username command-line]
    "Execute given command if it is found in local namespace with a correct arity. Return the new prompt-prefix."
    (let [parts (clojure.string/split command-line #" ")
          command (first parts)
          params (rest parts)
          command-fun (resolve (symbol "cardgame.commands" command))]
        (when-not (= "quit" command)
            (if-not command-fun
                (do (println (str "Command '" command "' not found."))
                    prompt-prefix)
                (let [arities (->> command-fun meta :arglists (map count))]
                    (if (= -1 (.indexOf arities (count params)))
                        (do (println (str "Wrong number of arguments for '" command "'."))
                            prompt-prefix)
                        (apply command-fun params)))))))

