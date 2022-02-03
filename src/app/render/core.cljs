(ns app.render.core
  (:require [app.engine.core :refer [update-board flip-cell board-state]]))


(defn cell-onclick [y x cell-value]
  (fn [] (update-board (flip-cell y x))))

(defn select-cell-color [cell-value]
  (if cell-value "white" "yellow"))

(defn cell-style [cell-value]
  {:background-color (select-cell-color cell-value) :height "10px" :width "10px" :cursor "pointer"})

(defn cell [y]
  (fn [x cell-value] ^{:key x}
    [:div {:style (cell-style cell-value)
           :on-click (cell-onclick y x cell-value)}]))

(defn row [y row]
  [:div {:key y} (map-indexed (cell y) row)])

(defn board []
  [:div {:style {:display "flex"}}
   (map-indexed row @board-state)])