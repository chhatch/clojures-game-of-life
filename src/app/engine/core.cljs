(ns app.engine.core
  (:require [reagent.core :as r]
            [cljs.core.async :refer [go timeout <!]]
            [clojure.string :as str])
  (:require [app.engine.utils :refer [cell-key flip-cell gen-adjecent-keys]]))

(def game-running (r/atom false))
(def rows 100)
(def cols 100)

(def cell-keys (flatten
                (for [y (range rows)]
                  (for [x (range cols)]
                    (cell-key y x)))))

(def init-cell-values (repeatedly (* rows cols) #(r/atom 0)))
(def board-state (apply assoc {} (interleave cell-keys init-cell-values)))

(defn cell-value
  ([cell-key] @(cell-key board-state))
  ([y x] @((cell-key y x) board-state)))

(def cells-adjacent (apply assoc {} (interleave cell-keys (repeatedly (* rows cols) (gen-adjecent-keys rows cols)))))

(defn sum-adjacent [y x]
  (apply + (map cell-value ((cell-key y x) cells-adjacent))))

(defn update-cell [adjacent-sum cell-alive cell-atom y x]
  (if cell-alive
    (if-not (or (= adjacent-sum 2) (= adjacent-sum 3)) true false)
    (if (= adjacent-sum 3) true false)))

(defn cell-should-update [updates cell-key]
  (let [[y x] (str/split (name cell-key) #"-")
        adjacent-sum (sum-adjacent y x)
        cell-alive (= (cell-value y x) 1)
        cell-atom (cell-key board-state)]
    (if (update-cell adjacent-sum cell-alive cell-atom y x) (conj updates cell-atom) updates)))

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