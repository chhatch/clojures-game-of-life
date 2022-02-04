(ns app.engine.core
  (:require [reagent.core :as r]
            [cljs.core.async :refer [go timeout <!]]))

(def board-size 100)
(def board-state (r/atom (repeat board-size (repeat board-size false))))
(def game-running (r/atom false))

(defn update-board [new-state]
  (swap! board-state (fn [] new-state)))

(defn flip-cell [y x]
  (map-indexed (fn [y2 col] (map-indexed (fn [x2 cell-value] (if (and (= y y2) (= x x2)) (not cell-value) cell-value)) col)) @board-state))


(defn start-loop []
  (go (while @game-running
        (<! (timeout 1000))
        (println "I should print once per second"))))

(defn start-stop-game []
  (swap! game-running #(not @game-running))
  (if @game-running (start-loop)))