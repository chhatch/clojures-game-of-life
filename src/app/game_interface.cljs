(ns app.game-interface
  (:require [app.render.core :as render]))

(defn game-interface []
  [:div {:style {:display "flex" :justify-content "center"}}
   [:div {:style {:display "flex" :flex-direction "column" :border "solid"}}
    [render/board]
    [render/buttons]]])
