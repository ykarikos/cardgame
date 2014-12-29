(ns cardgame.commands
    "Commands that can be issued in the prompt. The first parameter of each command is the current game state. Each command should return the new state."
    (:require [cardgame.decks.core :as decks]))

(defn create-game [state name players decktype]
    (let [deck (decks/get-deck decktype)]
        (when deck
            {:name name
             :playercount (read-string players)
             :players (list (:username state)) ; maybe username@host ?
             :joined 1
             :deck (shuffle deck)})))

(defn- game-full? [state]
    (let [playercount (:playercount state)
          joined (:joined state)]
        (and joined playercount (= joined playercount))))

(defn- get-rest [v n]
    (if (> n (count v))
        []
        (subvec v n)))

(defn deal [state cardcount]
    (if-not (game-full? state)
        (println "Waiting for more players. Can't deal cards yet.")
        (let [number-of-cards-to-deal (* (read-string cardcount) (:playercount state))
              dealt-cards (take number-of-cards-to-deal (:deck state))
              rest-deck (get-rest (:deck state) number-of-cards-to-deal)]
            (println "Deal " dealt-cards)
            {:deck rest-deck})))

; join from remote
(defn join [state username]
    (if (game-full? state)
        (println "Game full. Can not join.")
        {:joined (+ 1 (:joined state))
         :players (cons username (:players state))}))


(defn quit [state]
    nil)

(defn execute [command params]
    "Execute given command if it is found in local namespace with a correct arity. Return the new state."
    (let [command-fun (resolve (symbol "cardgame.commands" command))
          arities (->> command-fun meta :arglists (map count))]
        (if-not command-fun
            (println (str "Command '" command "' not found."))
            (if (= -1 (.indexOf arities (count params)))
                (println (str "Wrong number of arguments for '" command "'."))
                (apply command-fun params)))))

