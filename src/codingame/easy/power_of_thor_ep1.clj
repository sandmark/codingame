(ns codingame.easy.power-of-thor-ep1
  (:require [codingame.core :refer [output debug]]
            [clojure.string :as str]))

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
