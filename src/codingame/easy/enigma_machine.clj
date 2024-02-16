(ns codingame.easy.enigma-machine
  (:require [clojure.set :as set]))

;; During World War II, the Germans were using an encryption code called Enigma
;; which was basically an encryption machine that encrypted messages for transmission.
;; The Enigma code went many years unbroken. Here's How the basic machine works:
;;
;; First Caesar shift is applied using an incrementing number:
;; If String is AAA and starting number is 4 then output will be EFG.
;; A + 4 = E
;; A + 4 + 1 = F
;; A + 4 + 1 + 1 = G
;;
;; Now map EFG to first ROTOR such as:
;; ABCDEFGHIJKLMNOPQRSTUVWXYZ
;; BDFHJLCPRTXVZNYEIWGAKMUSQO
;; So EFG becomes JLC. Then it is passed through 2 more rotors to get the final value.
;;
;; If the second ROTOR is AJDKSIRUXBLHWTMCQGZNPYFVOE, we apply the substitution step again thus:
;; ABCDEFGHIJKLMNOPQRSTUVWXYZ
;; AJDKSIRUXBLHWTMCQGZNPYFVOE
;; So JLC becomes BHD.
;;
;; If the third ROTOR is EKMFLGDQVZNTOWYHXUSPAIBRCJ, then the final substitution is:
;; ABCDEFGHIJKLMNOPQRSTUVWXYZ
;; EKMFLGDQVZNTOWYHXUSPAIBRCJ
;; So BHD becomes KQF.
;;
;; Final output is sent via Radio Transmitter.

;; Input
;;
;; Line 1: ENCODE or DECODE
;; Line 2: Starting shift N
;; Lines 3-5:
;; BDFHJLCPRTXVZNYEIWGAKMUSQO ROTOR I
;; AJDKSIRUXBLHWTMCQGZNPYFVOE ROTOR II
;; EKMFLGDQVZNTOWYHXUSPAIBRCJ ROTOR III
;; Line 6: Message to Encode or Decode

(def az-seq (->> (range (int \A) (inc (int \Z))) (map char)))

(defn nth-cycle [coll i]
  (cond (> i (count coll))      (nth (cycle coll) i)
        (or (zero? i) (pos? i)) (nth (cycle coll) i)
        (neg? i)                (nth (cycle (reverse coll)) (dec (abs i)))))

(defn caesar-shift [n s]
  (->> (seq s)
       (map-indexed (fn [i c] (+ i n (int c) (- (int \A)))))
       (map (partial nth-cycle az-seq))
       (apply str)))

(defn caesar-unshift [n s]
  (->> (seq s)
       (map-indexed (fn [i c] (+ (- i) (- n) (int c) (- (int \A)))))
       (map (partial nth-cycle az-seq))
       (apply str)))

(defn make-rotor [s]
  (zipmap az-seq (seq s)))

(defn encode [rotors s]
  (->> rotors
       (reduce #(map %2 %1) (seq s))
       (apply str)))

(defn decode [rotors s]
  (->> rotors
       (map set/map-invert)
       (reduce #(map %2 %1) (seq s))
       (apply str)))

(defn input []
  (-> *in* java.io.BufferedReader. line-seq))

(defn enigma [enc-dec n' r1 r2 r3 s]
  (let [n      (Integer. n')
        rotors (map make-rotor [r1 r2 r3])]
    (if (= enc-dec "ENCODE")
      (->> (caesar-shift n s)
           (encode rotors))
      (->> (decode (reverse rotors) s)
           (caesar-unshift n)))))

(defn output [s]
  (println s)
  (flush))
