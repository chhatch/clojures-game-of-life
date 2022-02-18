(ns app.engine.core
  (:require [cljs.core.async :refer [go timeout <!]]
            [clojure.string :as str])
  (:require [app.engine.utils :refer [flip-cell sum-adjacent get-cell-value]])
  (:require [app.engine.state :refer [game-running cell-keys board-state cells-adjacent]]))

(defn update-required [adjacent-sum cell-alive]
  (if cell-alive
    (if-not (or (= adjacent-sum 2) (= adjacent-sum 3)) true false)
    (if (= adjacent-sum 3) true false)))

(defn cell-should-update [board-state]
  (fn [updates cell-key]
    (let [[y x] (str/split (name cell-key) #"-")
          adjacent-sum ((sum-adjacent board-state cells-adjacent) cell-key)
          cell-alive (= ((get-cell-value board-state) y x) 1)
          cell-atom (cell-key board-state)]
      (if (update-required adjacent-sum cell-alive) (conj updates cell-atom) updates))))

(defn update-list []
  (reduce (cell-should-update board-state) [] cell-keys))

(defn start-loop []
  (go (while @game-running
        (<! (timeout 0))
        (doseq [cell (update-list)]
          (flip-cell cell)))))

(defn start-stop-game []
  (swap! game-running #(not @game-running))
  (if @game-running (start-loop)))