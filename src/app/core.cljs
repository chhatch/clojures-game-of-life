(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [app.game-interface :refer [game-interface]]))

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [game-interface] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic."
  []
  (render))
