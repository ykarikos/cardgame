(ns cardgame.commands.remote
    "Commands that can be issued remotely."
    (:require [cardgame.state :as state]))

(defn join [username]
    (if (state/game-full?)
        {:msg "Game full. Can not join."}
        {:command "state"
         :msg "Joined."
         :state (state/join-player username)}))

