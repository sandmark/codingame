(ns codingame.easy.playing-card-odds
  (:require [tupelo.core :as t]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn read-input []
  (let [ls      (-> *in* java.io.BufferedReader. line-seq vec)
        [rm st] (map #(Integer/parseInt %) (str/split (first ls) #" "))
        rms     (->> ls (drop 1) (take rm))
        soughts (->> ls (drop 1) (drop rm) (take st))]
    {:rms rms :soughts soughts}))

(defn output [n]
  (println (str n "%")))

(def ^:private RANKS
  {"2" 2 "3" 3 "4" 4 "5" 5 "6" 6 "7" 7 "8" 8 "9" 9 "T" 10 "A" 1 "J" 11 "Q" 12 "K" 13})

(def ^:private SUITS
  {"S" :spade "D" :diamond "H" :heart "C" :club})

(def ^:private DEFAULT-RANKS (range 1 14))
(def ^:private DEFAULT-SUITS [:spade :club :diamond :heart])

(defn- create-full-deck []
  (set (for [rank DEFAULT-RANKS, suit DEFAULT-SUITS] [rank suit])))

(defn- extract-values [s mapping default-values regex]
  (let [extracted (->> (re-seq regex s) (map mapping) seq)]
    (or extracted default-values)))

(defn- parse-ranks [s]
  (extract-values s RANKS DEFAULT-RANKS #"[2-9ATJQK]"))

(defn- parse-suits [s]
  (extract-values s SUITS DEFAULT-SUITS #"[CDHS]"))

(defn- calculate-card-probability [removed-deck target-set]
  (let [possibilities (count (set/intersection removed-deck target-set))
        card-count    (count removed-deck)]
    (cond
      (zero? possibilities)        0
      (= possibilities card-count) 100
      :else                        (-> (/ possibilities card-count) (* 100) float Math/round))))

(defn- parse-classification [s]
  (set (for [rank (parse-ranks s), suit (parse-suits s)] [rank suit])))

(defn solve [{:keys [rms soughts]}]
  (let [full-deck    (create-full-deck)
        removed-deck (set/difference full-deck (set (mapcat parse-classification rms)))
        target-set   (set (mapcat parse-classification soughts))]
    (calculate-card-probability removed-deck target-set)))

(comment
  (defn -main [& _]
    (-> (read-input) solve output)))
