(ns codingame.easy.mars-lander-ep1
  (:require [codingame.core :refer [output debug]]
            [clojure.string :as str]))

;;;
;;; Mars Lander - Episode 1
;;; https://www.codingame.com/ide/puzzle/mars-lander-episode-1
;;;
;;; ストーリー:
;;; 火星探査機 - レベル1 - ファーストコンタクト
;;; 2003年6月21日、午前2時、ケープカナベラル。ケネディ宇宙センターの閑散とした廊下に、大きな声が響き渡る。
;;; 「マイク、どうなってる！ 離陸まで1ヶ月を切っているにも関わらず、確実な解決策が未だ見付からないとは！"
;;; 四角い顎に軍人らしい髪型のジェフは、火星オポチュニティ作戦の責任者であり、今まさにチーフエンジニアに詰め寄っているのだ。
;;; 「でもシミュレーションの結果、成功率は99%だよ」
;;; 「話にならん。1%は無視できないリスクだ。引き当てないことを祈りでもするのか？
;;;  忘れるなよ。もしオポチュニティが墜落したら、お前も俺も揃って地獄行きなんだからな」
;;; 「NASAのトップ・エンジニアがもう取り掛かってる。これ以上ない人材じゃないか！」
;;; 「そうか？ 外部リソースをあたってみよう。もっとマシな解決策があるかどうか確かめるんだ」
;;;
;;; * 目標
;;; プログラムの目標は、オポチュニティ探査機を搭載した着陸シャトル「マーズランダー」を安全に着陸させること。
;;; マーズランダーはプログラムに従って誘導されるが、NASAのシミュレーターによれば、現在の着陸失敗率は許容しがたい。
;;; 難しい問題のように見えるが、実際には簡単に解ける問題だ。
;;; このパズルは3つあるレベルの最初のものであるため、あとあと必要になる操作をいくつか紹介しなければならない。
;;;
;;; * ルール
;;; ゲームとして作成されたシミュレーターでは、マーズランダーは限られた空域に展開される。
;;; 空域の幅は7000m, 高さは3000mだ。
;;; このレベルに限っては、マーズランダーは着陸帯の上空に存在し、初速はない。
;;; 火星の表面には最低でも1000mの平らな地面がある。
;;;
;;; プログラムは毎秒、現在の飛行状況（位置、速度、燃料など）に応じて、
;;; マーズランダーに新たな傾斜角と推力を指示しなければならない。
;;; 傾斜角は-90度から90度、推力は0から4までだ。
;;; このレベルに限っては推力のみ制御すればいい。傾斜角は常に0となる。
;;;
;;; ゲームでは大気のない状況下での自由落下をシミュレートする。
;;; 火星の重力は 3.711 m/s^2 だ。推力が X であるとき X m/s^2 に等しい押力が発生し、
;;; また X リットルの燃料が消費される。そのため火星の重力に反発するには
;;; ほぼ垂直姿勢かつ推力4が必要となる。
;;;
;;; 着陸成功には、船が次の状態でなければならない:
;;;   - 平らな地面への着地
;;;   - 垂直着陸（傾斜角0）
;;;   - 垂直速度 <= 40m/s （絶対値）
;;;   - 水平速度 <= 20m/s （絶対値）
;;;
;;; このパズルは簡略版である:
;;;   - 着陸地点は真下。そのため傾斜角は常に0で良い。
;;;   - 火星表面の座標を保持しておく必要はない。
;;;   - 垂直速度は 0 - 40m/s の間でよく、水平速度は考慮されない。
;;;   - シャトルが下降するとき垂直速度はマイナスになり、上昇するときプラスになる。
;;;
;;; 備考
;;; 今回のレベルでは、マーズランダーが受けるテストは1つだけ。
;;; テストとバリデーターの違いはほんのわずかであり、
;;; テストに合格したプログラムは、バリデーターにも問題なく合格する。
;;;
;;; 入力
;;; プログラムはまず、標準入力から初期化データを読み込まなければならない。
;;; 次に、無限ループの中で、プログラムはマーズランダーの現在の状態に関連するデータを標準入力から読み取り、
;;; マーズランダーを動かす命令を標準出力に提供しなければならない。
;;;
;;; 初期入力
;;; Line 1:
;;;   surfaceX: 火星の表面を表すのに使われる座標の数
;;; 続く surfaceX 行:
;;;   地表の座標を表す整数 landX, landY 。
;;;   座標を順番に結んでいくことで、火星の表面の状態がわかる。
;;;   最初の座標は landX=0, 最後の座標は landX=6999 となる。
;;;
;;; 1ターンごとの入力
;;; 7つの整数からなる1行: X, Y, hSpeed, vSpeed, fuel, rotate, power.
;;;   X,Y: マーズランダーの座標（単位はメートル）
;;;   hSpeed, vSpeed: マーズランダーの水平速度と垂直速度。向きによってはマイナスになる。
;;;   fuel: 燃料の残量（リットル）。燃料がなくなると推力はゼロになる。
;;;   rotate: マーズランダーの傾斜角。
;;;   power: マーズランダーの推力。
;;;
;;; 1ターンごとの出力
;;; 2つの整数からなる1行: rotate, power.
;;;   rotate: マーズランダーの回転角度。各ターン最大 +/- 15度に制限される。
;;;   power:  指定推力。0はオフ、4が最大。各ターン最大 +/- 1に制限される。
;;;
;;; 例
;;; 初期入力
;;; 6         	(surfaceN) Surface made of 6 points
;;; 0 1500    	(landX landY)
;;; 1000 2000	(landX landY)
;;; 2000 500	(landX landY) Start of flat ground
;;; 3500 500	(landX landY) End of flat ground
;;; 5000 1500	(landX landY)
;;; 6999 1000	(landX landY)
;;;
;;; Input for turn 1
;;; 2500 2500 0 0 500 0 0  	(X Y hSpeed vSpeed fuel rotate power)
;;;
;;; Input for turn 2
;;; 2500 2499 0 -3 499 0 1 	(X Y hSpeed vSpeed fuel rotate power)
;;;
;;; Input for turn 3
;;; 2500 2495 0 -4 497 0 2 	(X Y hSpeed vSpeed fuel rotate power)
;;;

(defn string->numbers [s]
  (->> (str/split s #"\s")
       (map #(Integer/parseInt %))))

(defn get-turn-input [in]
  (let [[x y h v fuel rotate power] (string->numbers (.readLine in))]
    {:x x :y y :h h :v v :fuel fuel :rotate rotate :power power}))

(defn get-init-input [in]
  (let [n (Integer/parseInt (.readLine in))]
    (->> (repeatedly n #(.readLine in))
         (map string->numbers))))

(defn control-output [rotate power]
  (output (str rotate " " power)))

(def mg 3.711)

(defn next-vspeed [v power]
  (+ (- mg) v power))

(defn inc-power [power]
  (if (= power 4)
    power
    (inc power)))

(defn mlep1 []
  (with-open [in *in*]
    (let [_ (doall (get-init-input in))]
      (while true
        (let [{:keys [v power]} (get-turn-input in)]
          (if (< (next-vspeed v power) -35)
            (control-output 0 (inc-power power))
            (control-output 0 0)))))))
