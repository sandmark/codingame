(ns codingame.easy.scrabble-score
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [tupelo.core :as t]))

(defn parse-tiles
  "
  (parse-tiles [\"A 1\" \"B 3\"])
  ;; => {\\A 1 \\B 3}
  "
  [lines]
  (into {} (for [l lines :let [[c i] (str/split l #" ")]]
             [(first c) (Integer/parseInt i)])))

(defn mapv-2d
  "
  (mapv-2d inc [[1 2 3] [4 5 6]])
  ;; => [[2 3 4] [5 6 7]]

  (mapv-2d vector [[1 2 3] [4 5 6]] [[:a :b :c] [:d :e :f]])
  ;; => [[[1 :a] [2 :b] [3 :c]]
  ;; =>  [[4 :d] [5 :e] [6 :f]]]
  "
  [f & colls]
  (let [inner-f (fn [& xs] (apply f xs))
        outer-f (fn [& inner-colls] (apply mapv inner-f inner-colls))]
    (apply mapv outer-f colls)))

(defn parse-cells
  "
  (parse-cells [\"W.w\" \".lL\"])
  ;; => [[{:multiplier :triple-word} {:multiplier nil}            {:multiplier :double-word}]
  ;; =>  [{:multiplier nil}          {:multiplier :double-letter} {:multiplier :triple-letter}]]
  "
  [lines]
  (let [multiplify {\W :triple-word \w :double-word \l :double-letter \L :triple-letter}]
    (mapv-2d (fn [x] {:multiplier (multiplify x)}) lines)))

(defn put-op-tiles [{:keys [tiles cells]} lines]
  (let [f (fn [cell letter]
            (cond-> (assoc cell :letter letter :score (tiles letter))
              (= letter \.)    (assoc :letter nil)
              (not= letter \.) (dissoc :multiplier)))]
    (mapv-2d f cells lines)))

(defn calc-letter-score [tiles multiplier letter]
  (cond-> (or (tiles letter) 0)
    (= multiplier :double-letter) (* 2)
    (= multiplier :triple-letter) (* 3)))

(defn assoc-mine-flag [{:keys [letter] :as cell} new-letter]
  (cond-> (assoc cell :letter new-letter)
    (not= letter new-letter) (assoc :mine true)
    (= new-letter \.)        (dissoc :letter)))

(defn put-my-tile [tiles {:keys [multiplier] :as cell} new-letter]
  (let [score (calc-letter-score tiles multiplier new-letter)]
    (cond-> (-> cell (assoc-mine-flag new-letter) (assoc :score score))
      (or (= multiplier :double-letter)
          (= multiplier :triple-letter)) (assoc :multiplier nil))))

(defn put-my-tiles [{:keys [tiles op-board]} lines]
  (mapv-2d (partial put-my-tile tiles) op-board lines))

(defn split-all [pred items]
  (when (seq items)
    (apply conj (reduce (fn [[acc curr] x]
                          (if (pred x)
                            [(conj acc curr) [x]]
                            [acc (conj curr x)]))
                        [[] []] items))))

(defn rotate
  "
  (rotate [[1 2 3] [4 5 6] [7 8 9]])
  ;; => [[1 4 7] [2 5 8] [3 6 9]]
  "
  [v]
  (apply mapv vector v))

(defn find-my-words-linear
  "
  (find-my-words-linear [[{:letter nil} {:letter \\O} {:letter \\F :mine true} {:letter nil}]
                         [{:letter nil} {:letter \\R :mine true} {:letter nil} {:letter nil}]])
  ;; => [[{:letter \\O} {:letter \\F :mine true}]]

  (find-my-words-linear [[{:letter \\O} {:letter \\F}]])
  ;; => []

  (find-my-words-linear [[{:letter \\O :mine true}]
                         [{:letter \\F :mine true}]])
  ;; => []
  "
  [board]
  (->> (interpose [{:letter nil}] board)
       (flatten)
       (split-all (comp not :letter))
       (map (partial filter :letter))
       (filter (partial some :mine))
       (filterv #(>= (count %) 2))))

(defn find-my-words
  "
  (find-my-words [[{:letter \\O} {:letter \\F :mine true}]
                  [{:letter \\R :mine true} {}]])
  ;; => [[{:letter \\O} {:letter \\F :mine true}] [{:letter \\O} {:letter \\R :mine true}]]

  (find-my-words [[{:letter \\O} {:letter \\F}]
                  [{:letter \\R :mine true} {}]])
  ;; => [[{:letter \\O} {:letter \\R :mine true}]]
  "
  [board]
  (concat (find-my-words-linear board)
          (find-my-words-linear (rotate board))))

(defn read-input []
  (let [lines      (vec (line-seq (java.io.BufferedReader. *in*)))
        tiles-cnt  (-> lines first Integer/parseInt)
        height     (let [l (->> lines (drop (inc tiles-cnt)) first)]
                     (-> l (str/split #" ") last Integer/parseInt))
        tiles-l    (->> lines (drop 1) (take tiles-cnt))
        cells-l    (->> lines (drop (+ 2 tiles-cnt)) (take height))
        op-board-l (->> lines (drop (+ 2 tiles-cnt height)) (take height))
        my-board-l (->> lines (drop (+ 2 tiles-cnt height height)) (take height))
        tiles      (parse-tiles tiles-l)
        cells      (parse-cells cells-l)
        op-board   (put-op-tiles {:tiles tiles :cells cells} op-board-l)
        my-board   (put-my-tiles {:tiles tiles :op-board op-board} my-board-l)]
    {:tiles    tiles
     :cells    cells
     :op-board op-board
     :my-board my-board}))

(defn cells->word [cells]
  (let [word  (->> cells (map :letter) str/join)
        score (->> cells (map :score) (reduce + 0))
        mults (->> cells (keep :multiplier) (map {:double-word 2 :triple-word 3}))]
    {:word word :score (reduce * score mults)}))

(defn solve [{:keys [my-board]}]
  (let [my-words    (->> my-board find-my-words (map cells->word))
        my-cell-cnt (->> my-board flatten (filter (every-pred :letter :mine)) count)
        bonus       (cond-> 0 (>= my-cell-cnt 7) (+ 50))]
    {:words (sort-by :word my-words)
     :bonus bonus
     :total (->> my-words (map :score) (reduce + 0) (+ bonus))}))

(defn output [{:keys [words bonus total]}]
  (doseq [{:keys [word score]} words]
    (println (format "%s %s" word score)))
  (when-not (zero? bonus)
    (println "Bonus 50"))
  (println (format "Total %s" total)))

(comment
  (defn -main [& _]
    (-> (read-input) solve output)))
