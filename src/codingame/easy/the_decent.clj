(ns codingame.easy.the-decent
  (:require [codingame.core :refer [output debug]]))

;;;
;;; Puzzle/The Decent
;;; https://www.codingame.com/ide/puzzle/the-descent
;;;
;;; 標準入力から8行にわたって Integer が渡される。
;;; もっとも大きな数値のインデックスを標準出力せよ。
(defn parse-integers
  "標準入力からn行読み込んで `Integer/parseInt` したコレクションを返す。"
  [n]
  (->> (line-seq (java.io.BufferedReader. *in*))
       (take n)
       (map #(Integer/parseInt %))))

(defn max-index
  "与えられた `coll` のうち、もっとも大きな値のインデックスを返す。"
  [coll]
  (->> coll
       (map-indexed vector)
       (sort-by second >)
       ffirst))

(defn the-decent []
  (while true
    (->> (parse-integers 8)
         max-index
         output)))
