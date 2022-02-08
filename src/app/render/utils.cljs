(ns app.render.utils
  (:require [app.engine.utils :refer [cell-key flip-cell sum-adjacent cells-adjacent]]))


(defn cell-onclick [y x cell-atom]
  (fn []
    (println (cell-key y x) (sum-adjacent y x) ((cell-key y x) cells-adjacent))
    (flip-cell cell-atom)))

(defn select-cell-color [cell-value]
  (if (= cell-value 0) "white" "yellow"))

(defn cell-style [cell-value]
  {:background-color (select-cell-color cell-value) :height "10px" :width "10px" :cursor "pointer"})