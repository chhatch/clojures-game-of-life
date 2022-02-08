(ns app.engine.state)

(def game-running (r/atom false))
(def rows 100)
(def cols 100)
; todo: don't do this ever again
(def board-state ((resolve 'app.utils/init-board-state)))