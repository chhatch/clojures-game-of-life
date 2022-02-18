(ns app.engine.utils)

(defn get-cell-key [y x]
  (keyword (str y "-" x)))

(defn get-cell-value [board-state]
  (fn ([cell-key] @(cell-key board-state))
    ([y x] @((get-cell-key y x) board-state))))


(defn flip-0-1 [n]
  (mod (inc n) 2))

(defn flip-cell [atom]
  (swap! atom flip-0-1))

(defn sum-adjacent [board-state cells-adjacent]
  (fn [cell-key]
    (apply + (map (get-cell-value board-state) (cell-key cells-adjacent)))))