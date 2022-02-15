(ns app.game-interface
  (:require [app.render.core :as render])
  (:require [app.engine.state :refer [game-running]]))

(defn testier []
  (println "testier rendered")
  (if @game-running "game-running", "game-stopped"))
(defn tester []
  (println "tester rendered")
  (testier))

(defn game-interface []
  (println "interface rendered")
  [:div {:style {:display "flex" :justify-content "center"}}
   [:div {:style {:display "flex" :flex-direction "column" :border "solid"}}
    [render/board]
    [render/start-stop-button]
    [tester]]])
