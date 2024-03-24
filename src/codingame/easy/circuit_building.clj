(ns codingame.easy.circuit-building
  (:require [clojure.string :as str]
            [clojure.walk :as walk]))

(defmulti resistance #(cond (vector? %) ::parallel
                            (list? %)   ::series
                            :else       ::value))
(defmethod resistance ::parallel [rs] (/ 1 (apply + (map (partial / 1) rs))))
(defmethod resistance ::series   [rs] (apply + rs))
(defmethod resistance ::value    [x] x)

(defn parse [m s]
  (->> s read-string (walk/postwalk-replace m)))

(defn calc [tree]
  (walk/postwalk resistance tree))

(defn lines->map [ls]
  (into {} (for [l ls :let [[s n] (str/split l #"\s")]] [(symbol s) (Integer. n)])))

(defn solve [vars-lines circuit]
  (format "%.1f" (-> vars-lines lines->map (parse circuit) calc float)))

(defn input []
  (let [[n & lseq]       (-> *in* java.io.BufferedReader. line-seq)
        [vars [circuit]] (split-at (Integer. n) lseq)]
    [vars circuit]))

(defn output [x]
  (println x)
  (flush))
