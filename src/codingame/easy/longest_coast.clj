(ns codingame.easy.longest-coast
  (:require [tupelo.core :as t]))

(def offsets [[1 0] [-1 0] [0 1] [0 -1]])

(defn neighbours [[y x] {:keys [grid]}]
  (->> offsets
       (map (fn [[dy dx]] [(+ y dy) (+ x dx)]))
       (filter (partial get-in grid))))


(defn search-island [coord grid]
  (letfn [(search-fn [[q v r]]
            (let [[coord q'] ((juxt peek pop) q)]
              (cond (contains? v coord)        [q' v r]
                    (= \~ (get-in grid coord)) [q' v (update r :ocean conj coord)]
                    (= \# (get-in grid coord)) [(apply conj q' (neighbours coord {:grid grid}))
                                                (conj v coord)
                                                (update r :land conj coord)])))]
    (->> [(conj clojure.lang.PersistentQueue/EMPTY coord) #{} {:land #{} :ocean #{}}]
         (iterate search-fn)
         (drop-while (comp not empty? first))
         first
         (drop 2))))

(defn distinct-by
  "Returns a stateful transducer that removes elements by calling f on each step as a uniqueness key.
   Returns a lazy sequence when provided with a collection."
  ([f]
   (fn [rf]
     (let [seen (volatile! #{})]
       (fn
         ([] (rf))
         ([result] (rf result))
         ([result input]
          (let [v (f input)]
            (if (contains? @seen v)
              result
              (do (vswap! seen conj v)
                  (rf result input)))))))))
  ([f xs]
   (sequence (distinct-by f) xs)))

(defn search-islands [{:keys [size grid]}]
  (->> grid
       (search-island [y x])
       (for [y (range 0 size), x (range 0 size)])
       flatten
       (filter (comp seq :land))
       (distinct-by :land)))

(defn solve [data]
  (->> data
       search-islands
       (map-indexed #(vector (inc %1) (count (:ocean %2))))
       (sort-by second >)
       first))

(defn output [[idx cnt]]
  (println (str idx cnt)))

(defn parse-grid [lines]
  (mapv vec lines))

(defn read-input []
  (let [ls   (-> *in* java.io.BufferedReader. line-seq vec)
        size (-> ls first Integer/parseInt)
        grid (parse-grid (next ls))]
    {:size size, :grid grid, :island-index 1}))

(comment
  (defn -main [& _]
    (-> (read-input) solve output)))
