(ns codingame.easy.sudoku-validator
  "Input
  9 rows of 9 space-separated digits representing the sudoku grid.

  Output
  true or false

  Constraints
  For each digit n in the grid: 1 ≤ n ≤ 9.

  Example
  Input
  1 2 3 4 5 6 7 8 9
  4 5 6 7 8 9 1 2 3
  7 8 9 1 2 3 4 5 6
  9 1 2 3 4 5 6 7 8
  3 4 5 6 7 8 9 1 2
  6 7 8 9 1 2 3 4 5
  8 9 1 2 3 4 5 6 7
  2 3 4 5 6 7 8 9 1
  5 6 7 8 9 1 2 3 4

  Output
  true"
  (:require [clojure.string :as str]))

(defn transpose [matrix]
  (apply mapv vector matrix))

(defn block [matrix n]
  (let [row-start (* (quot n 3) 3)
        col-start (* (rem n 3) 3)]
    (for [i (range 3), j (range 3)]
      (get-in matrix [(+ row-start i) (+ col-start j)]))))

(defn valid? [matrix]
  (let [valid-set? (partial = #{1 2 3 4 5 6 7 8 9})
        rows       (map set matrix)
        cols       (map set (transpose matrix))
        blocks     (->> (range 0 9) (map (comp set (partial block matrix))))]
    (and (every? valid-set? rows)
         (every? valid-set? cols)
         (every? valid-set? blocks))))


(defn input []
  (let [ls         (-> *in* java.io.BufferedReader. line-seq)
        parse-int  #(Integer. %)
        parse-nums #(->> (str/split % #"\s") (mapv parse-int))]
    (->> ls (mapv parse-nums) (into []))))

(defn output [bool]
  (println bool)
  (flush))
