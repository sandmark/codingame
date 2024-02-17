(ns codingame.easy.ghost-legs
  (:require [clojure.string :as str]))

(defn ->dir [[left _ right]]
  (cond (= left \-)  dec
        (= right \-) inc
        :else        identity))

(defn parse-routes [lines]
  (letfn [(enspace [s] (str " " s " "))]
    (->> lines
         (map enspace)
         (map seq)
         (map (partial partition 3))
         (map (partial map ->dir)))))

(defn search-entry [n routes]
  (reduce (fn [i route] ((nth route i) i)) n routes))

(defn search [entries goals routes]
  (letfn [(f [i e]
            (let [n (search-entry i routes)]
              (vector e (nth goals n))))]
    (map-indexed f entries)))

(defn solve [lines]
  (let [[width height] (->> (str/split (first lines) #"\s") (map #(Integer. %)))
        routes         (-> lines vec (subvec 2 height) parse-routes)
        entries        (filter seq (-> lines second (str/split #"\s")))
        goals          (filter seq (-> lines last (str/split #"\s")))]
    (->> (search entries goals routes)
         (map (partial apply str))
         (str/join "\n"))))

(defn input []
  (-> *in* java.io.BufferedReader. line-seq))

(defn output [s]
  (println s)
  (flush))
