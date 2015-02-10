(ns blue-terminator.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [blue-terminator.draw :as d]))

(defn setup []
  (q/frame-rate 30)
  ;; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ;;  initial state
  {:color 0
   :angle 0})

(q/defsketch blue-terminator
  :title "You spin my circle right round"
  :size [500 500]
  :setup setup
  ;; update-state is called on each iteration before draw-state.
  :update d/update-state
  :draw d/draw-state
  :middleware [m/fun-mode])
