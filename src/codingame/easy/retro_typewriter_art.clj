(ns codingame.easy.retro-typewriter-art
  (:require [codingame.core :refer [output debug]]
            [clojure.string :as str]))

;;
;; Retro TypeWriter Art
;; https://www.codingame.com/ide/puzzle/retro-typewriter-art
;;
;; 昔、人々がまだタイプライターを使っていたころ、とある「レシピ」を驚くべきイメージに変換して楽しんでいました。
;; 与えられたレシピから、認識できるイメージを作成しましょう。
;;
;; レシピの構成要素はスペース記号で区切られます。
;; 構成要素は `\newline` (キャリッジリターン) を表す `nl` か、
;; どの文字をどれだけ描画するかを指示します。
;;
;; 例:
;; `4z` は `zzzz`
;; `1{` は `{`
;; `10=` は `==========`
;; `5bS` は `\\\\\` (略語については下記)
;; `27` は `77`
;; `123` は `333333333333`
;; (構成要素に数しか含まれていない場合、文字は最後の数字になります)
;;
;; つまりレシピの一部がこんな感じだった場合:
;; `2* 15sp 1x 4sQ nl`
;; 次のように描画し、改行します。
;; `**               x''''``
;;
;; 略語一覧:
;;   - `sp`: スペース
;;   - `bS`: バックスラッシュ `\`
;;   - `sQ`: シングルクォート `'`
;;   - `nl`: 改行
;;
;; Sources/references:
;; https://asciiart.cc
;; https://loriemerson.net/2013/01/18/d-i-y-typewriter-art/
;; https://www.youtube.com/watch?v=kyK5WvpFxqo
;;

(def abbrevs {"bS" "\\" "nl" "\n" "sQ" "'" "sp" " "})

(defn expand [ch]
  (let [[_ n' s'] (re-find #"(\d*)(.+)" ch)
        n         (if (seq n') (Integer. n') 1)
        s         (get abbrevs s' s')]
    (apply str (repeat n s))))

(defn render [recipe]
  (->> (str/split recipe #"\s")
       (map expand)
       (apply str)))

(defn retro-aa []
  (output (render (read-line))))
