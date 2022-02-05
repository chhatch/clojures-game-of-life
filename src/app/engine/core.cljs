(ns app.engine.core
  (:require [reagent.core :as r]
            [cljs.core.async :refer [go timeout <!]]
            [clojure.string :as str]))

(def rows 100)
(def cols 100)

(defn cell-key [y x]
  (keyword (str y "-" x)))

(def cell-keys (flatten
                (for [y (range rows)]
                  (for [x (range cols)]
                    (cell-key y x)))))

(def cell-values (repeatedly (* rows cols) #(r/atom 0)))
(def board-state (apply assoc {} (interleave cell-keys cell-values)))
(def game-running (r/atom false))

(defn flip-cell [atom]
  (swap! atom #(mod (inc @atom) 2)))

(defn gen-cell-neighbors []
  (let [y (atom 0) x (atom 0)]
    (fn [] (let [up (if (= @y 0) (dec rows) (dec @y))
                 down (if (= @y (dec rows)) 0 (inc @y))
                 right (if (= @x (dec cols)) 0 (inc @x))
                 left (if (= @x 0) (dec cols) (dec @x))
                 here-y @y here-x @x]
             ; update y
             (if (= @x (dec cols)) (swap! y inc))
             ;update x
             (swap! x #(mod (inc %) cols))
             ; starting at 12 o'clock going clockwise
             [(cell-key up here-x)
              (cell-key up right)
              (cell-key here-y right)
              (cell-key down right)
              (cell-key down here-x)
              (cell-key down left)
              (cell-key here-y left)
              (cell-key up left)]))))

(def cells-adjacent (apply assoc {} (interleave cell-keys (repeatedly (* rows cols) (gen-cell-neighbors)))))

(defn cell-value
  ([cell-key] @(cell-key board-state))
  ([y x] @((cell-key y x) board-state)))

(defn sum-adjacent [y x]
  (apply + (map cell-value ((cell-key y x) cells-adjacent))))

(defn update-cell [adjacent-sum cell-alive cell-atom y x]
  (if cell-alive
    (if-not (or (= adjacent-sum 2) (= adjacent-sum 3)) true false)
    (if (= adjacent-sum 3) true false)))

(defn update-list []
  (reduce (fn [updates cell-key]
            (let [[y x] (str/split (name cell-key) #"-")
                  adjacent-sum (sum-adjacent y x)
                  cell-alive (= (cell-value y x) 1)
                  cell-atom (cell-key board-state)]
              (if (update-cell adjacent-sum cell-alive cell-atom y x) (conj updates cell-atom) updates)))
          [] cell-keys))

(defn start-loop []
  (go (while @game-running
        (<! (timeout 250))
        (println "test")
        (doseq [update-atom (update-list)]
          (swap! update-atom #(mod (inc %) 2))))))

(defn start-stop-game []
  (swap! game-running #(not @game-running))
  (if @game-running (start-loop)))