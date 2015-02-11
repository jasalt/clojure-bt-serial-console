;; Draw ns can be reloaded easily in REPL 
(ns blue-terminator.draw
  (:require [quil.core :as q]))

(defn random-fill []
  (q/fill (q/random 255) (q/random 255) (q/random 255))
  )

(defn draw-bar [bar-x-pos, height]
  (random-fill)
  (let [bottom-pos 300
        draw-height (* 10 height)
        bar-y-pos (- bottom-pos draw-height)]
    (q/rect bar-x-pos bar-y-pos 30 draw-height)
    )
  )

(defn draw-bars [state]
  (draw-bar 100 (state :left-sensor))
  (draw-bar 200 (state :front-sensor))
  (draw-bar 300 (state :right-sensor))
  )

(defn draw-state [state]
  ;; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (draw-bars state)
  )

(defn update-state [state]
  {:left-sensor (q/random 10)
   :right-sensor (q/random 10)
   :front-sensor (q/random 10)
   :robot-state "unknown"
   }
  ) 
