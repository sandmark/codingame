(ns codingame.easy.card-counting-easy
  (:require [clojure.string :as str]))

(def deck (into {} (for [x [\2 \3 \4 \5 \6 \7 \8 \9 \A \T \J \Q \K]] [x 4])))
(def abbrev (into {} (map vector (range 1 14) [\A \2 \3 \4 \5 \6 \7 \8 \9 \T \J \Q \K])))

(defn draw-card [deck card]
  (update deck card #(some-> % dec)))

(defn valid-cards? [s]
  (every? deck s))

(defn consciousness->cards [consciousness]
  (->> (str/split consciousness #"\.") (filter valid-cards?) (mapcat seq)))

(defn remove-cards [deck cards]
  (reduce draw-card deck cards))

(defn count-total [deck]
  (reduce + (vals deck)))

(defn count-under-bust [deck bust]
  (->> (range 1 bust) (map abbrev) (map deck) (reduce +)))

(defn solve [{:keys [bust consciousnesses-str]}]
  (let [cards        (->> (str/split consciousnesses-str #"\s") (mapcat consciousness->cards))
        current-deck (remove-cards deck cards)
        total        (count-total current-deck)
        under-bust   (count-under-bust current-deck bust)]
    (format "%d%%" (Math/round (* (float (/ under-bust total)) 100)))))

(defn input []
  (let [[s b] (-> *in* java.io.BufferedReader. line-seq)]
    {:bust                (Integer. b)
     :consciousnesses-str s}))

(defn output [s]
  (println s)
  (flush))
