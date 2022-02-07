(ns app.engine.utils)

(defn cell-key [y x]
  (keyword (str y "-" x)))

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