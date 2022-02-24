(ns app.engine.core
  (:require [cljs.core.async :refer [go timeout <!]])
  (:require [app.engine.utils :refer [flip-cell sum-adjacent get-cell-value set-0 set-1]])
  (:require [app.engine.state :refer [game-running cell-keys board-state cells-adjacent]]))

(defn update-required [adjacent-sum cell-alive]
  (if cell-alive
    (if-not (or (= adjacent-sum 2) (= adjacent-sum 3)) true false)
    (if (= adjacent-sum 3) true false)))

(defn cell-should-update [board-state]
  (fn [cell-key]
    (if (update-required ((sum-adjacent board-state cells-adjacent) cell-key) (= ((get-cell-value board-state) cell-key) 1)) cell-key)))

(defn update-list []
  (doall (keep (cell-should-update board-state) cell-keys)))

(defn start-loop []
  (go (while @game-running
        (<! (timeout 0))
        (doseq [cell-key (update-list)]
          (flip-cell (cell-key board-state))))))

(defn start-stop-game []
  (swap! game-running #(not @game-running))
  (if @game-running (start-loop)))

(defn random-board []
  (doseq [[cell-key cell-atom] board-state]
    (if (< (rand-int 10) 3) (swap! cell-atom set-1) (swap! cell-atom set-0))))

(defn clear-board []
  (doseq [[cell-key cell-atom] board-state]
    (swap! cell-atom set-0)))