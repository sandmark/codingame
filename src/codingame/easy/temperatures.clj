(ns codingame.easy.temperatures
  (:require [codingame.core :refer [debug output]]
            [clojure.string :as str]))

;;;
;;; Temperatures
;;; https://www.codingame.com/ide/puzzle/temperatures
;;;
;;; stdin:
;;;   N
;;;   1 -5 2 -3 4
;;;
;;; Investigate a number which is closest to zero.
(defn temperatures-input []
  (let [[n s] (line-seq (java.io.BufferedReader. *in*))]
    (if (= n "0")
      []
      (map #(Integer/parseInt %)
           (str/split s #"\s")))))

(defn find-closest-zero [ns]
  (if (empty? ns)
    0
    (let [ns+abs        (->> ns
                             (map #(vector (abs %) %))
                             (sort-by first))
          closest-abs   (ffirst ns+abs)
          [[_ a] [_ b]] (take-while (fn [[abs]] (= abs closest-abs)) ns+abs)]
      (if b (max a b) a))))

(defn temperatures []
  (output (find-closest-zero (temperatures-input))))
