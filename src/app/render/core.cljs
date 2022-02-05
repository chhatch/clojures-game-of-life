(ns app.render.core
  (:require [app.engine.core :as engine :refer [rows cols flip-cell board-state]]))


(defn cell-onclick [y x cell-atom]
  (fn []
    (println (engine/cell-key y x) (engine/sum-adjacent y x) ((engine/cell-key y x) engine/cells-adjacent))
    (engine/flip-cell cell-atom)))

(defn select-cell-color [cell-value]
  (if (= cell-value 0) "white" "yellow"))

(defn cell-style [cell-value]
  {:background-color (select-cell-color cell-value) :height "10px" :width "10px" :cursor "pointer"})

(defn cell [y x cell-atom]
  ^{:key x} [:div {:style (cell-style @cell-atom)
                   :on-click (cell-onclick y x cell-atom)}])

(defn board []
  [:div {:style {:display "flex" :flex-wrap "wrap" :width 1000}}
   (doall (for [y (range rows)]
            (doall (for [x (range cols)]
                     (let [cell-atom ((keyword (str y "-" x)) board-state)]
                       (cell y x cell-atom))))))])

(defn start-stop-button []
  [:button {:on-click engine/start-stop-game} (if @engine/game-running "Stop" "Start")])