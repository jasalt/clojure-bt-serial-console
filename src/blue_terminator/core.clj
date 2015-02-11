(ns blue-terminator.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [blue-terminator.draw :as d]))

(def initial-state {:left-sensor 0.0
                    :right-sensor 0.0
                    :front-sensor 0.0
                    :robot-state "unknown"
                    })

(defn setup []
  (q/frame-rate 10)
  ;; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ;;  initial state
  {:color 0
   :angle 0}
  initial-state)

(q/defsketch blue-terminator
  :title "You spin my circle right round"
  :size [500 500]
  :setup setup
  ;; update-state is called on each iteration before draw-state.
  :update d/update-state
  :draw d/draw-state
  :middleware [m/fun-mode])
