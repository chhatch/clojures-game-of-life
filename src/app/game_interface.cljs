(ns app.game-interface
  (:require [reagent.core :as r]))

(def board-size 100)
(def board-state (r/atom (repeat board-size (repeat board-size false))))

(defn update-board [new-state]
  (swap! board-state (fn [] new-state)))

(defn flip-cell [y x]
  (map-indexed (fn [y2 col] (map-indexed (fn [x2 cell-value] (if (and (= y y2) (= x x2)) (not cell-value) cell-value)) col)) @board-state))

(defn cell-onclick [y x cell-value]
  (fn [] (update-board (flip-cell y x))))

(defn select-cell-color [cell-value]
  (if cell-value "white" "yellow"))

(defn cell-style [cell-value]
  {:background-color (select-cell-color cell-value) :height "10px" :width "10px" :cursor "pointer"})

(defn renderCell [y]
  (fn [x cell-value] ^{:key x}
    [:div {:style (cell-style cell-value)
           :on-click (cell-onclick y x cell-value)}]))

(defn renderRow [y row]
  [:div {:key y} (map-indexed (renderCell y) row)])

(defn renderBoard []
  [:div {:style {:display "flex"}}
   (map-indexed renderRow @board-state)])

(defn game-interface []
  [:div {:style {:display "flex" :justify-content "center"}}
   [:div {:style {:display "flex" :flex-direction "column" :border "solid"}} [renderBoard]]])
