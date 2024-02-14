(ns codingame.easy.spreadsheet-1d
  (:require [codingame.core :refer [debug]]
            [clojure.string :as str]))

(defn input []
  (next (line-seq (java.io.BufferedReader. *in*))))

(defn parse [i e]
  (letfn [(wrap [s]
            (if (str/starts-with? s "$")
              {:ref (Integer. (apply str (rest s)))}
              {:value (Integer. s)}))]
    (let [[op & args] (str/split e #"\s")]
      (case op
        "VALUE" (-> (wrap (first args)) (assoc :idx i))
        "ADD"   {:idx i :op :add :args (map wrap args)}
        "SUB"   {:idx i :op :sub :args (map wrap args)}
        "MULT"  {:idx i :op :mult :args (map wrap args)}))))

(defn make-resolver [coll]
  (let [cache (volatile! {})
        ops   {:add + :sub - :mult *}]
    (fn resolve-fn [{:keys [idx] :as e}]
      (letfn [(unwrap [arg]
                (if (:ref arg)
                  (resolve-fn (nth coll (:ref arg)))
                  (:value arg)))
              (apply-op [args]
                (apply (ops (:op e)) (map unwrap args)))
              (evaluate []
                (cond (:value e) (:value e)
                      (:ref e)   (resolve-fn (nth coll (:ref e)))
                      :else      (apply-op (:args e))))]
        (if-let [cached-value (get @cache idx)]
          cached-value
          (let [result (evaluate)]
            (vswap! cache assoc idx result)
            result))))))

(defn resolve-cells [coll]
  (let [parsed (map-indexed parse coll)
        f      (make-resolver parsed)]
    (map f parsed)))

(defn output [coll]
  (->> coll (str/join "\n") println)
  (flush))
