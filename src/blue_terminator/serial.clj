(ns blue-terminator.serial
  (:require
   [clojure.core.async :as a :refer [go chan <! >! <!! >!!]]
   [serial.core :as serial]
   [serial.util :refer [list-ports]]
   ))

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
       (let [byte (<! from-chan)]
         (println (str "Got: " byte)))
       (recur)))))

(defn receive-fn [serial-input-chan]
  "Reads input bytes and puts them to queue."
  (fn [b]
    (>!! serial-input-chan (.read b))))

(defn initialize-serial []
  (def port (serial/open "ttyACM0" :baud-rate 9600))
  (def serial-input-chan (chan))
  (def pchan (print-messages (parse-messages serial-input-chan)))
  (def rfn (receive-fn serial-input-chan))
  (serial/listen port rfn nil))

(defn cleanup [port]
  (serial/remove-listener port))
