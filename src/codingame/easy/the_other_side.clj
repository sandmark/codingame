(ns codingame.easy.the-other-side
  (:require [clojure.string :as str]
            [tupelo.core :as t]))

(defn parse-grid [ls]
  (mapv #(str/split % #" ") ls))

(defn read-input []
  (let [ls   (->> *in* java.io.BufferedReader. line-seq vec)
        h    (-> ls first Integer/parseInt)
        w    (-> ls second Integer/parseInt)
        grid (->> ls (drop 2) parse-grid)]
    {:width w :height h :grid grid}))

(defn bfs [{:keys [width grid]} start]
  (let [offsets [[0 1] [-1 0] [1 0] [0 -1]]
        init-q  clojure.lang.PersistentQueue/EMPTY
        init-v  #{}]
    (letfn [(neighbours [[y x]]
              (map (fn [[dy dx]] [(+ y dy) (+ x dx)]) offsets))
            (terminated? [[q]]
              (or (empty? q)
                  (let [[_ x :as coord] (peek q)
                        val             (get-in grid coord)]
                    (and (= val "+")
                         (= x (dec width))))))
            (search [[q v]]
              (let [q'    (pop q)
                    coord (peek q)
                    val   (get-in grid coord)]
                (cond (contains? v coord) [q' v]
                      (= val "+")         [(apply conj q' (neighbours coord))
                                           (conj v coord)]
                      :else               [q' (conj v coord)])))]
      (->> [(conj init-q start) init-v]
           (iterate search)
           (drop-while (comp not terminated?))
           ffirst))))

(defn reachable? [data coord]
  (seq (bfs data coord)))

(defn solve [{:keys [height] :as data}]
  (let [leftmosts (for [y (range height)] [y 0])]
    (count (keep (partial reachable? data) leftmosts))))

(defn -main [& _]
  (-> (read-input) solve println))
