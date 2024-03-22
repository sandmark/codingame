(ns codingame.easy.drunken-bishop
  (:require [clojure.string :as str]))

(def dirs {2r00 [-1 -1]
           2r01 [ 1 -1]
           2r10 [-1  1]
           2r11 [ 1  1]})

(def init-x 8)
(def init-y 4)
(def width 16)
(def height 8)
(def symbols [\space \. \o \+ \= \* \B \O \X \@ \% \& \# \/ \^])

(defn bits->pos
  "Returns moved coordinates within range from 0 upto {width, height}.

  Examples:
    - from [1 1] to top-left should be like below. nothing special:
      (bits->pos 2r00 1 1)
      ;; => [0 0]

    - from [0 0] to top-left should be fixed:
      (bits->pos 2r00 0 0)
      ;; => [0 0]

    - from [0 1] to top-left should be slided:
      (bits->pos 2r00 0 1)
      ;; => [0 0]
  "
  [bits x y]
  (let [in-range      (fn [n limit] (max 0 (min n limit)))
        [new-x new-y] (map + [x y] (dirs bits))]
    [(in-range new-x width) (in-range new-y height)]))

(defn hex->bits
  "Returns bit-seq generated from hex-seq.

  (hex->bits [0xfc 0x94])
  ;; => [2r00 2r11 2r11 2r11, 2r00 2r01 2r01 2r10]
  "
  [hex-seq]
  (letfn [(->bits [hex] (map #(-> hex (bit-shift-right %) (bit-and 2r11)) [0 2 4 6]))]
    (mapcat ->bits hex-seq)))

(defn input
  "Returns hex sequence from splitted string in stdin.

  (with-in-str \"fc:94\n\"
    (input))
  ;; => [0xfc 0x94]
  "
  []
  (->> (str/split (read-line) #":")
       (map #(Integer/parseInt % 16))))

(defn coords-seq
  "Returns a sequence of accumulated coordinates using `bits->pos`.
  In case of initial values x=8, y=4; then the result should be like below.

  (coords-seq 8 4 [3 3 3 3 3 3 3 3 3])
  ;; => [[8 4] [9 5] [10 6] [11 7] [12 8] [13 8] [14 8] [15 8] [16 8] [16 8]]
  "
  [x y bits-seq]
  (reductions (fn [[px py] step] (bits->pos step px py)) [x y] bits-seq))

(defn drunken-bishop [hex-seq]
  (let [coords        (coords-seq init-x init-y (hex->bits hex-seq))
        [end-x end-y] (last coords)
        atrium        (-> (vec (repeat (inc height) (vec (repeat (inc width) 0)))) (assoc-in [init-y init-x] nil))
        f             (fn [m [x y]] (update-in m [y x] #(some-> % inc)))]
    (-> (reduce f atrium coords) (assoc-in [init-y init-x] \S) (assoc-in [end-y end-x] \E))))

(defn coords->symbols [v]
  (let [syms    (cycle symbols)
        replace (fn [x] (if (int? x) (nth syms x) x))]
    (map replace v)))

(defn decorate [vs]
  (let [new-vs (mapv (partial format "|%s|") vs)]
    (concat ["+---[CODINGAME]---+"] new-vs ["+-----------------+"])))

(defn solve [hex-seq]
  (->> hex-seq drunken-bishop (map coords->symbols) (map (partial apply str)) decorate))

(defn output [lines]
  (println (str/join "\n" lines))
  (flush))
