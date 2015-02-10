(ns blue-terminator.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [blue-terminator.draw :as d]))

(defn setup []
                                        ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
                                        ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
                                        ; setup function returns initial state. It contains
                                        ; circle color and position.
  {:color 0
   :angle 0})

(q/defsketch blue-terminator
  :title "You spin my circle right round"
  :size [500 500]
                                        ; setup function called only once, during sketch initialization.
  :setup setup
                                        ; update-state is called on each iteration before draw-state.
  :update d/update-state
  :draw d/draw-state
                                        ; This sketch uses functional-mode middleware.
                                        ; Check quil wiki for more info about middlewares and particularly
                                        ; fun-mode.
  :middleware [m/fun-mode])
