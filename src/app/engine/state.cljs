(ns app.engine.state
  (:require [reagent.core :as r])
  (:require [app.engine.utils :refer [cell-key]]))
(def game-running (r/atom false))
(def rows 100)
(def cols 100)


(def init-cell-values (repeatedly (* rows cols) #(r/atom 0)))

(def cell-keys (flatten
                (for [y (range rows)]
                  (for [x (range cols)]
                    (cell-key y x)))))

(def board-state (apply assoc {} (interleave cell-keys init-cell-values)))

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