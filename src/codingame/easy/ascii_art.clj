(ns codingame.easy.ascii-art
  (:require [codingame.core :refer [debug output]]
            [clojure.string :as str]))

(defn to-seq-2d [width height text]
  (letfn [(partition-letter [l]
            (->> l (partition width) (map (partial apply str))))]
    (for [l (str/split-lines text)] (partition-letter l))))

(defn fontify [width height text]
  (letfn [(register [m [i x]]
            (let [ch (if (= i 26)
                       \?
                       (char (+ i 65)))]
              (update m ch (fnil conj []) x)))]
    (->> (to-seq-2d width height text)
         (map #(map-indexed vector %))
         flatten
         (partition 2)
         (reduce register {}))))

(defn str->keys [s]
  (let [valid-keys (->> (range 65 (+ 65 26)) (map char))]
    (->> (seq (str/upper-case s))
         (map #(if (some #{%} valid-keys) % \?)))))

(defn render [m s]
  (->> s
       str->keys
       (map #(get m %))
       (map concat)
       (apply map vector)
       (map str/join)
       (str/join "\n")))

(defn ascii-art []
  (let [width  (Integer/parseInt (read-line))
        height (Integer/parseInt (read-line))
        s      (read-line)
        text   (->> (line-seq (java.io.BufferedReader. *in*))
                    (str/join "\n"))
        font   (fontify width height text)]
    (output (render font s))))
