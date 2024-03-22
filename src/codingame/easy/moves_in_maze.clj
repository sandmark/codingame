(ns codingame.easy.moves-in-maze
  (:require [clojure.string :as str]))

(defn input []
  (let [[wh & maze] (-> *in* java.io.BufferedReader. line-seq)
        [w h]       (->> (str/split wh #"\s") (mapv #(Integer. %)))]
    {:w w, :h h, :maze (mapv vec maze)}))

(defn find-start [{:keys [w h maze]}]
  (first (for [x     (range w)
               y     (range h)
               :when (= \S (get-in maze [y x]))]
           [x y])))

(defn neighbours [{:keys [w h]} x y step]
  (for [[rel-x rel-y] [[0 -1] [0 1] [-1 0] [1 0]]]
    [(mod (+ x rel-x) w) (mod (+ y rel-y) h) (inc step)]))

(defn wall? [maze x y]
  (= (get-in maze [y x]) \#))

(defn visited? [maze x y]
  (int? (get-in maze [y x])))

(defn path? [maze x y]
  (some #{(get-in maze [y x])} #{\S \.}))

(defn bfs [m [start-x start-y]]
  (loop [queue                (conj clojure.lang.PersistentQueue/EMPTY [start-x start-y 0])
         {:keys [maze] :as p} m]
    (let [[x y step] (peek queue)]
      (cond (path? maze x y)    (recur (apply conj (pop queue) (neighbours p x y step))
                                       (assoc-in p [:maze y x] step))
            (empty? queue)      p
            (wall? maze x y)    (recur (pop queue) p)
            (visited? maze x y) (recur (pop queue) p)))))

(defn solve [m]
  (let [{:keys [maze]} (bfs m (find-start m))
        ->base36       #(cond (not (int? %)) %
                              (< % 10)       (char (+ % 48))
                              :else          (char (+ (- % 10) 65)))]
    (mapv #(mapv ->base36 %) maze)))

(defn output [vec2d]
  (doseq [l (->> vec2d (map (partial apply str)))]
    (println l))
  (flush))
