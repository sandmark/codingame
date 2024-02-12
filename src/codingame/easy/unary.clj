(ns codingame.easy.unary
  (:require [clojure.string :as str]
            [clojure.pprint :as p]
            [codingame.core :refer [output debug]]))
;;;
;;; Rules:
;;;   - 入力メッセージは 7-bit ASCII
;;;   - エンコードされた出力メッセージは0のブロックで構成
;;;   - ブロック同士はスペースで区切られる
;;;   - 2つのブロックはセットで、同じ値のビットを表現する
;;;     - 第一要素: 常に 0 か 00。0 であれば値 1 を表し、00 であれば値 0 を表す。
;;;     - 第二要素: いくつ同じ値が続くかを 0 の数で表す。
;;;
;;; Example:
;;; 大文字の C はバイナリで 1000011 である。これをエンコードすると:
;;;   - 0 0 (第一部分は 1 が 1 つ)
;;;   - 00 0000 (第二部分は 0 が 4 つ)
;;;   - 0 00 (第三部分は 1 が 2 つ)
;;; となり、 C は 0 0 00 0000 0 00 である。
;;;

(defn char->binary [c]
  (->> (p/cl-format nil "~7,'0b" (int c))
       seq
       (map #(Integer/parseInt (str %)))))

(defn series->block [[x :as series]]
  (vector (if (zero? x) "00" "0")
          (apply str (repeat (count series) "0"))))

(defn str->unary [s]
  (->> (seq s)
       (mapcat char->binary)
       (partition-by zero?)
       (mapcat series->block)
       (str/join " ")))

(defn unary []
  (output (str->unary (read-line))))
