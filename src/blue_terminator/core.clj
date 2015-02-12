(ns blue-terminator.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [blue-terminator.draw :as d]
            [blue-terminator.serial :as connection]
            ))



(def initial-state {:left-sensor 0.0
                    :right-sensor 0.0
                    :front-sensor 0.0
                    :robot-state "unknown"
                    :autopilot nil
                    })

(defn setup []
  (q/smooth)
  (q/frame-rate 10)
  ;; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ;;  initial state
  (def port (try
    (connection/initialize-serial)
    (catch Exception e nil)))
  ;; :commander is a function that takes string and sends it to serial port.
  (assoc initial-state :commander (partial connection/send-command port)))

(defn key-press [state, event]
  (println (str event))
  (cond
    (= (:key event) :w) ((:commander state) "<F:256>")
    (= (:key event) :s) ((:commander state) "<B:256>")
    (= (:key event) :a) ((:commander state) "<L:256>")
    (= (:key event) :d) ((:commander state) "<R:256>")
    :else ((:commander state) "<F:0>")))


(q/defsketch blue-terminator
  :title "You spin my circle right round"
  :size [500 500]
  :setup setup
  ;; update-state is called on each iteration before draw-state.
  :update d/update-state
  :draw d/draw-state
  :key-typed key-press
  :middleware [m/fun-mode])
