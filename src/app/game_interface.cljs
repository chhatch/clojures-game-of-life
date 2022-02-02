(ns app.game-interface
  (:require [reagent.core :as r]))

(def board-size 100)
(def board-state (r/atom (repeat board-size (repeat board-size false))))

(defn update-board [new-state]
  (swap! board-state (fn [] new-state)))

(defn flip-cell [y x]
  (map-indexed (fn [y2 col] (map-indexed (fn [x2 cell-value] (if (and (= y y2) (= x x2)) (not cell-value) cell-value)) col)) @board-state))

(defn cell-onclick [y x]  
  (fn [] (println "row-" y " col-" x " value: " (nth (nth @board-state y) x))
    (update-board (flip-cell y x))))

(defn renderCell [y x]
  [:div {:style {:background-color "yellow" :height "10px" :width "10px" :cursor "pointer"} :on-click (cell-onclick y x)}])

(defn renderRow [y]
  [:div (for [x (range board-size)] ^{:key (str "col-" y "-" x)} [renderCell y x])])

(defn renderBoard []
  [:div {:style {:display "flex"}}
   (for [y (range board-size)]
     ^{:key (str "row-" y)} [renderRow y])])

(defn game-interface []
  [:div {:style {:display "flex" :justify-content "center"}} [:div {:style {:display "flex" :flex-direction "column" :border "solid"}}[renderBoard]]])
