(ns codingame.easy.mime-type
  (:require [codingame.core :refer [output debug]]
            [clojure.string :as str]))

(defn make-dict [coll]
  (->> coll
       (map #(str/split % #"\s"))
       (reduce (fn [m [k v]] (assoc m (str/lower-case k) v)) {})))

(defn file-ext [filename]
  (when (str/includes? filename ".")
    (last (str/split filename #"\." -1))))

(defn get-mime-type [m filename]
  (let [ext (some-> (file-ext filename) str/lower-case)]
    (get m ext "UNKNOWN")))

(defn read-lines [n]
  (repeatedly n #(.readLine *in*)))

(defn mime-type []
  (let [mcount (-> (.readLine *in*) Integer/parseInt)
        pcount (-> (.readLine *in*) Integer/parseInt)
        mtypes (make-dict (read-lines mcount))]
    (doseq [filename (read-lines pcount)]
      (output (get-mime-type mtypes filename)))))
