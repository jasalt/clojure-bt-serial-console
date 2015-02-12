(ns blue-terminator.serial
  (:require
   [clojure.core.async :as a :refer [go chan <! >! <!! >!!]]
   [serial.core :as serial]
   [serial.util :refer [list-ports]]
   ))

(def sensor-state (atom {}))

(defn parse-messages
  "Listen channel for serial commands in form `<S:12:23:22>',
  parse them and push to result-channel."
  ([c]
   (let [result-chan (chan)]
     (go (loop [byte (<! c)
                result [] buffer []]

           (cond
             (== byte (int \<)) (do (recur (<! c) [] []))
             (== byte (int \:)) (do (recur (<! c)
                                           (conj result (apply str buffer)) []))
             (== byte (int \>))
             (let [cmd-val (first result)
                   vals (rest (conj result (apply str buffer)))]
               (>! result-chan {:cmd cmd-val :vals vals})
               (recur (<! c) [] []))

             :else (recur (<! c) result (conj buffer (char byte))))))
     result-chan)))

(defn print-messages
  "Prints incoming messages type from from-chan."
  ([from-chan]
   (go
     (loop []
       (let [sensor-readings (<! from-chan)]
         (println (str "Got: " sensor-readings))
         (reset! sensor-state sensor-readings)
         )
       (recur)))))

(defn receive-fn [serial-input-chan]
  "Reads input bytes and puts them to queue."
  (fn [b]
    (>!! serial-input-chan (.read b))))

(defn send-command [port command]
  "Sends command to serial port."
  ;; TODO: this probably doesn't work
  (serial/write port command))

(defn initialize-serial []
  ;; TODO read and set port automagically
  (def port (serial/open "cu.usbmodemfa131" :baud-rate 9600))
  (def serial-input-chan (chan))
  (def pchan (print-messages (parse-messages serial-input-chan)))
  (def rfn (receive-fn serial-input-chan))
  (serial/listen port rfn nil)
  port)

(defn cleanup [port]
  (serial/remove-listener port)
  (serial/close port)
  )
