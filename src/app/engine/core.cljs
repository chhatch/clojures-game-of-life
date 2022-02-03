(ns app.engine.core
  (:require [reagent.core :as r]))

(def board-size 100)
(def board-state (r/atom (repeat board-size (repeat board-size false))))

(defn update-board [new-state]
  (swap! board-state (fn [] new-state)))

(defn flip-cell [y x]
  (map-indexed (fn [y2 col] (map-indexed (fn [x2 cell-value] (if (and (= y y2) (= x x2)) (not cell-value) cell-value)) col)) @board-state))