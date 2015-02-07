(ns cardgame.commands.remote
    "Commands that can be issued remotely. Commands return the data that will be sent back."
    (:require [cardgame.state :as state]))

(defn join [username]
    (if (state/game-full?)
        {:msg "Game full. Can not join."}
        {:command "state"
         :msg "Joined."
         :params (state/join-player username)}))

(defn- get-rest [v n]
    (if (> n (count v))
        []
        (subvec v n)))

(defn- take-cards [deck cardcount playercount]
    (fn [skip]
        (take cardcount (take-nth playercount (get-rest deck skip)))))

(defn- deal-message [username cards]
    (str username " dealt you " (clojure.string/join ", " cards)))

(defn- deal-state-message [username cards]
    {:command "state"
     :msg (deal-message username cards)
     :params {:local {:hand cards}}})

(defn deal [username cardcount]
    "This function is run only on the server. It deals cards to all players,
    changes the local state and returns the message that is sent to the clients to change their state."
    (let [deck (-> @state/state :game :deck)
          playercount (-> @state/state :game :playercount)
          number-of-cards-to-deal (* cardcount playercount)
          dealt-cards (map (take-cards deck cardcount playercount) (range playercount))
          rest-deck-state {:game {:deck (get-rest deck number-of-cards-to-deal)}}
          server-dealer? (= username (-> @state/state :local :username))
          server-cards (if server-dealer? (second dealt-cards) (first dealt-cards))
          others-cards (if server-dealer? (first dealt-cards) (second dealt-cards))]
        ; TODO: support >2 players
        (state/change-state (merge {:local {:hand server-cards}} rest-deck-state))
        (state/print-msg (deal-message username server-cards))
        (deal-state-message username others-cards)))