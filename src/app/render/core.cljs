(ns app.render.core
  (:require [app.render.utils :refer [cell-style cell-onclick]])
  (:require [app.engine.core :refer [start-stop-game random-board clear-board]])
  (:require [app.engine.state :refer [rows cols game-running board-state]]))

(defn cell [y x cell-atom]
  [:div {:style (cell-style @cell-atom)
         :on-click (cell-onclick y x cell-atom)}])

(defn board []
  [:div {:style {:display "flex" :flex-wrap "wrap" :width 1000}}
   (doall (for [y (range rows)]
            (doall (for [x (range cols)]
                     (let [cell-atom ((keyword (str y "-" x)) board-state)]
                       ^{:key (str y "-" x)}
                       [cell y x cell-atom])))))])

(defn start-stop-button []
  [:button {:on-click start-stop-game} (if @game-running "Stop" "Start")])

(defn random-board-button []
  [:button {:on-click random-board} "Random Board"])

(defn clear-board-button []
  [:button {:on-click clear-board} "Clear Board"])

(defn buttons []
  [:div {:style {:display "flex" :flex-direction "row" :border-top "solid"}}
   [start-stop-button]
   [random-board-button]
   [clear-board-button]])