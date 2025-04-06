(ns codingame.easy.six-degrees-of-kevin-bacon
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [tupelo.core :as t]))

(def bacon "Kevin Bacon")

(defn graphify [coll]
  (let [s (set coll)
        f #(vector % (set/difference s #{%}))]
    (->> coll (map f) (into {}))))

(defn parse-cinema [l]
  (as-> l $
    (str/split $ #":")
    (second $)
    (str/split $ #",")
    (map str/trim $)
    (graphify $)))

(defn read-input []
  (let [ls    (-> *in* java.io.BufferedReader. line-seq vec)
        actor (first ls)
        graph (->> ls (drop 2) (map parse-cinema) (apply merge-with set/union))]
    {:actor actor, :graph graph}))

(defn bfs [start graph]
  (let [q (conj clojure.lang.PersistentQueue/EMPTY [start []])]
    (letfn [(find-path [actor co-actors path]
              (mapv (fn [co-actor] [co-actor (conj path actor)]) co-actors))
            (search [[q v]]
              (let [[actor path] (peek q)
                    q'           (pop q)]
                (if (contains? v actor)
                  [q' v]
                  [(apply conj q' (find-path actor (graph actor) path))
                   (conj v actor)])))
            (terminated? [[q]]
              (or (empty? q) (= (ffirst q) bacon)))]
      (->> [q #{}]
           (iterate search)
           (drop-while (comp not terminated?))
           ffirst
           first))))

(defn solve [{:keys [actor graph]}]
  (some-> (bfs actor graph) second count))

(defn output [n]
  (println n))

#_(defn -main [& _]
    (-> (read-input) solve output))
