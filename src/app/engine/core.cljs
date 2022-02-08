(ns app.engine.core
  (:require [cljs.core.async :refer [go timeout <!]]
            [clojure.string :as str])
  (:require [app.engine.utils :refer [flip-cell sum-adjacent cell-value cell-keys]])
  (:require [app.engine.state :refer [game-running board-state]]))

(defn update-required [adjacent-sum cell-alive]
  (if cell-alive
    (if-not (or (= adjacent-sum 2) (= adjacent-sum 3)) true false)
    (if (= adjacent-sum 3) true false)))

(defn cell-should-update [updates cell-key]
  (let [[y x] (str/split (name cell-key) #"-")
        adjacent-sum (sum-adjacent y x)
        cell-alive (= (cell-value y x) 1)
        cell-atom (cell-key board-state)]
    (if (update-required adjacent-sum cell-alive) (conj updates cell-atom) updates)))

(defn update-list []
  (reduce cell-should-update [] cell-keys))

(defn start-loop []
  (go (while @game-running
        (<! (timeout 250))
        (doseq [cell (update-list)]
          (flip-cell cell)))))

(defn start-stop-game []
  (swap! game-running #(not @game-running))
  (if @game-running (start-loop)))