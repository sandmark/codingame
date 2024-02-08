(ns codingame.core
  (:require [clojure.string :as str]))

(defn output [msg] (println msg) (flush))
(defn debug [msg] (binding [*out* *err*] (println msg) (flush)))

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

(comment
  (defn -main [& args]
    (the-decent)))

;;;
;;; Puzzle/Power of Thor - Episode 1
;;; https://www.codingame.com/ide/puzzle/power-of-thor-episode-1
;;;
;;; 標準入力から4つの数値 lightX, lightY, initialTX, initialTY が渡される。
;;; - lightX: ゴールのX座標
;;; - lightY: ゴールのY座標
;;; - initialTX: キャラクターのX座標
;;; - initialTY: キャラクターのY座標
;;;
;;; マップサイズは 40x18 で、左上が (0 0), 右下が (30 17) となる。
;;; キャラクターから見てゴールがどの方角にあるかを出力せよ。
;;;
;;; |-------+-------+-------|
;;; | [0 0] | [1 0] | [2 0] |
;;; |-------+-------+-------|
;;; | [0 1] | [1 1] | [2 1] |
;;; |-------+-------+-------|
;;; | [0 2] | [1 2] | [2 2] |
;;; |-------+-------+-------|
;;;

(def thor-angle {[-1 -1] "NW" [0 -1] "N" [1 -1] "NE"
                 [-1 0]  "W"  [0 0]  "*" [1 0]  "E"
                 [-1 1]  "SW" [0 1]  "S" [1 1]  "SE"})

(defn thor-get-input []
  (let [s (first (line-seq (java.io.BufferedReader. *in*)))]
    (->> (str/split s #"\s")
         (map #(Integer/parseInt %))
         (partition 2))))

(defn compare-pos
  "ベクター dest と current を比較し、関係演算子のベクターを返す。"
  {:arglists '([dest current])}
  [[x y] [x' y']]
  [(compare x x') (compare y y')])

(defn thor-direction
  "thor の座標から light の座標への方角を返す。"
  [thor light]
  (get thor-angle (compare-pos light thor)))

(defn thor-ep1 []
  (let [[light initial-thor] (thor-get-input)
        thor                 (volatile! initial-thor)]
    (while true
      (let [move (compare-pos light @thor)]
        (output (get thor-angle move))
        (vswap! thor #(mapv + % move))))))

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
  (let [s (last (line-seq (java.io.BufferedReader. *in*)))]
    (map #(Integer/parseInt %)
         (str/split s #"\s"))))

(defn find-closest-zero [ns]
  (let [ns+abs            (->> ns
                               (map #(vector (abs %) %))
                               (sort-by first))
        closest-abs       (ffirst ns+abs)
        [[_ neg] [_ pos]] (->> ns+abs
                               (take-while (fn [[abs]] (= abs closest-abs)))
                               distinct)]
    (or pos neg)))

(defn temperatures []
  (output (find-closest-zero (temperatures-input))))
