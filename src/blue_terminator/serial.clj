(ns blue-terminator.serial
  (:require
   [clojure.core.async :as a :refer [go chan <! >! <!! >!!]]
   [serial.core :as serial]
   [serial.util :refer [list-ports]]
   ))

;; Cheatsheet (TODO ascii-converter-function)
;; 60 is <
;; 58 is :
;; 62 is >

(defn parse-messages
  "Listen given channel for input bytes,
  parse them (todo) and put commands to result-channel."
  ([c]
   (let [result-chan (chan)]
     (go (loop [byte (<! c) result []]
           (cond
             (== byte 60) (do (println "< byte")
                              (recur (<! c) []))
             (== byte 62) (do (println "> byte")
                              (>! result-chan result)
                              (recur (<! c) []))
             :else (recur (<! c) (conj result byte)))))
     result-chan)))

(defn print-messages
  "Prints incoming messages type from from-chan."
  ([from-chan]
   (go
     (loop []
       (let [byte (<! from-chan)]
         (println (str "Got: " byte)))
       (recur)))))

(defn receive-fn [serial-input-chan]
  "Reads input bytes and puts them to queue."
  (fn [b]
    (>!! serial-input-chan (.read b))))

(defn initialize-serial []
  (defonce port (serial/open "ttyACM1" :baud-rate 9600))
  (def serial-input-chan (chan))
  (def pchan (print-messages (parse-messages serial-input-chan)))
  (def rfn (receive-fn serial-input-chan))
  (serial/listen port rfn nil))

(defn cleanup [port]
  (serial/remove-listener port))
