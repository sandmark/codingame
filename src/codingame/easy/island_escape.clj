(ns codingame.easy.island-escape
  (:require [tupelo.core :as t]
            [clojure.string :as str]))

(def offsets [[-1 0] [1 0] [0 -1] [0 1]])

(defn neighbours [[y x]]
  (map (fn [[dy dx]] [(+ y dy) (+ x dx)]) offsets))

(defn inside? [{:keys [size]} [y x]]
  (let [in-bounds? #(<= 0 % (dec size))]
    (and (in-bounds? y) (in-bounds? x))))

(defn reachable? [{:keys [grid]} h coord]
  (<= (abs (- (get-in grid coord) h)) 1))

(defn candidates [{:keys [grid] :as data} coord]
  (let [h (get-in grid coord)]
    (->> (neighbours coord)
         (filter (partial inside? data))
         (filter (partial reachable? data h)))))

(defn search [[q v data]]
  (let [coord (peek q)
        q'    (pop q)
        cds   (candidates data coord)]
    (if (contains? v coord)
      [q' v data]
      [(apply conj q' cds) (conj v coord) data])))

(defn ocean? [[y x] {:keys [size]}]
  (or (zero? x) (zero? y) (= x (dec size)) (= y (dec size))))

(defn terminated? [[q _ data]]
  (or (empty? q)
      (ocean? (peek q) data)))

(defn bfs [start data]
  (->> [(conj clojure.lang.PersistentQueue/EMPTY start) #{} data]
       (iterate search)
       (drop-while (comp not terminated?))
       ffirst
       peek))

(defn start-pos [n]
  (let [center (quot (dec n) 2)]
    [center center]))

(defn parse-grid [lines]
  (mapv (fn [l]
          (->> (str/split l #" ")
               (mapv #(Integer/parseInt %))))
        lines))

(defn read-input []
  (let [ls   (-> *in* java.io.BufferedReader. line-seq vec)
        size (->> ls first Integer/parseInt)
        grid (->> ls (drop 1) parse-grid)]
    {:size size, :grid grid}))

(defn solve [{:keys [size] :as data}]
  (if (bfs (start-pos size) data)
    "yes"
    "no"))

(comment
  (defn -main [& _]
    (-> (read-input) solve println)))
