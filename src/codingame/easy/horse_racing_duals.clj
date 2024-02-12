(ns codingame.easy.horse-racing-duals
  (:require [codingame.core :refer [output debug]]))

(defn closest-difference [coll]
  (let [->diff (comp abs (partial apply -))]
    (->> coll sort (partition 2 1) (map ->diff) (apply min))))

(defn duals []
  (->> (line-seq (java.io.BufferedReader. *in*))
       next
       (map #(Integer/parseInt %))
       closest-difference
       output))
