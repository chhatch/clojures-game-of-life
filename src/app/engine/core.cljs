(ns app.engine.core
  (:require [reagent.core :as r]
            [cljs.core.async :refer [go timeout <!]]))

(def rows 100)
(def cols 100)

(def cell-keys (flatten
                (for [row (range rows)]
                  (for [col (range cols)]
                    (keyword (str row "-" col))))))

(def cell-values (repeatedly (* rows cols) #(r/atom false)))
(def board-state (apply assoc {} (interleave cell-keys cell-values)))
(def game-running (r/atom false))

(defn flip-cell [atom]
  (swap! atom #(not @atom)))


(defn start-loop []
  (go (while @game-running
        (<! (timeout 1000))
        (println "I should print once per second"))))

(defn start-stop-game []
  (swap! game-running #(not @game-running))
  (if @game-running (start-loop)))