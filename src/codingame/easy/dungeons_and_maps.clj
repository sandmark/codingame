(ns codingame.easy.dungeons-and-maps
  (:require [clojure.string :as str]
            [tupelo.core :as t]))

(defn parse-2-integers [l]
  (as-> l $
    (str/split $ #" ")
    (map #(Integer/parseInt %) $)))

(defn read-input []
  (let [ls    (-> *in* java.io.BufferedReader. line-seq vec)
        [w h] (->> ls first parse-2-integers)
        [x y] (->> ls second parse-2-integers)
        grids (->> ls (drop 3) (partition h) (mapv vec))]
    {:width w :height h :start [x y] :grids grids}))

(def dirs {\^ [-1 0]
           \v [1 0]
           \> [0 1]
           \< [0 -1]})

(defn terminated? [[q _ grid]]
  (or (empty? q)
      (= \T (get-in grid (first (peek q))))))

(defn search [[q v grid]]
  (let [[coord path] (peek q)
        q'           (pop q)
        dcoord       (dirs (get-in grid coord))]
    (cond (contains? v coord) [q' v grid]
          dcoord              [(conj q' [(mapv + coord dcoord) (conj path coord)]) (conj v coord) grid]
          :else               [[] v grid])))

(defn bfs [grid start]
  (->> [(conj clojure.lang.PersistentQueue/EMPTY [start []]) #{} grid]
       (iterate search)
       (drop-while (comp not terminated?))
       ffirst
       peek))

(defn count-path [[coord path]]
  (when path
    (count (conj path coord))))

(defn solve [{:keys [grids start]}]
  (let [cnts (map (comp count-path bfs) grids (repeat start))]
    (if (every? nil? cnts)
      "TRAP"
      (->> cnts (map-indexed vector) (remove (comp nil? second)) (sort-by second) ffirst))))

(defn -main [& _]
  (-> (read-input) solve println))
