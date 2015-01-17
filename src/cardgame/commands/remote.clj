(ns cardgame.commands.remote
    "Commands that can be issued remotely. Commands return the data that will be sent back."
    (:require [cardgame.state :as state]))

(defn join [username]
    (if (state/game-full?)
        {:msg "Game full. Can not join."}
        {:command "state"
         :msg "Joined."
         :params (state/join-player username)}))

