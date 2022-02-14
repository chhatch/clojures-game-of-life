(ns app.engine.utils)

(defn cell-key [y x]
  (keyword (str y "-" x)))

(defn cell-value [board-state]
  (fn ([cell-key] @(cell-key board-state))
    ([y x] @((cell-key y x) board-state))))


(defn flip-0-1 [n]
  (mod (inc n) 2))

(defn flip-cell [atom]
  (swap! atom flip-0-1))

(defn sum-adjacent [board-state cells-adjacent]
  (fn [y x]
    (apply + (map (cell-value board-state) ((cell-key y x) cells-adjacent)))))