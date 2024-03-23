(ns codingame.easy.crop-circles
  (:require [clojure.string :as str]))

(def coords (zipmap (map char (range (int \a) (int \z))) (range)))
(def width 19)
(def height 25)
(def crops (vec (repeat height (vec (repeat width 1)))))
(def inst-map {:mow       (constantly 0)
               :plant-mow (partial bit-xor 1)
               :plant     (constantly 1)})

(defn circle-indices [x y size]
  (for [i     (range width), j (range height)
        :let  [dx (- x i), dy (- y j), distance (Math/sqrt (+ (* dx dx) (* dy dy)))]
        :when (<= distance (/ size 2))]
    [i j]))

(defn parse-instruction [s]
  (let [[_ cmd ax ay size-str] (re-find #"(PLANTMOW|PLANT)?([a-z])([a-z])(\d+)" s)
        cmd                    (case cmd "PLANT" :plant "PLANTMOW" :plant-mow nil :mow)]
    {:cmd  cmd
     :x    (-> ax first coords)
     :y    (-> ay first coords)
     :f    (inst-map cmd)
     :size (Integer. size-str)}))

(defn apply-instruction [crops s]
  (let [{:keys [x y size f]} (parse-instruction s)]
    (reduce #(update-in %1 (reverse %2) f) crops (circle-indices x y size))))

(defn solve [instructions]
  (reduce apply-instruction crops instructions))

(defn output [bit-vec]
  (let [->crop #(if (zero? %) "  " "{}")
        s      (->> bit-vec (map (comp str/join (partial map ->crop))) (str/join "\n"))]
    (println s)
    (flush)))

(defn input []
  (str/split (read-line) #"\s"))
