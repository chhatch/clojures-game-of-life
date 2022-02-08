(ns app.engine.utils
  (:require [reagent.core :as r])
  (:require [app.engine.state :refer [rows cols board-state]]))

(def init-cell-values (repeatedly (* rows cols) #(r/atom 0)))

(defn cell-key [y x]
  (keyword (str y "-" x)))

(defn cell-value
  ([cell-key] @(cell-key board-state))
  ([y x] @((cell-key y x) board-state)))

(def cell-keys (flatten
                (for [y (range rows)]
                  (for [x (range cols)]
                    (cell-key y x)))))

(defn flip-0-1 [n]
  (mod (inc n) 2))

(defn flip-cell [atom]
  (swap! atom flip-0-1))

(defn gen-adjecent-keys [rows cols]
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

(def cells-adjacent (apply assoc {} (interleave cell-keys (repeatedly (* rows cols) (gen-adjecent-keys rows cols)))))

(defn sum-adjacent [y x]
  (apply + (map cell-value ((cell-key y x) cells-adjacent))))
(def init-board-state (apply assoc {} (interleave cell-keys init-cell-values)))