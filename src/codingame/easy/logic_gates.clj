(ns codingame.easy.logic-gates
  "A logic gate is an electronic device implementing a boolean function,
  performing a logical operation on one or more binary inputs and producing a single binary output.

  Given n input signal names and their respective data,
  and m output signal names with their respective type of gate and two input signal names,
  provide m output signal names and their respective data, in the same order as provided in input description.

  All type of gates will always have two inputs and one output.
  All input signal data always have the same length.

  The type of gates are :
  AND : performs a logical AND operation.
  OR : performs a logical OR operation.
  XOR : performs a logical exclusive OR operation.
  NAND : performs a logical inverted AND operation.
  NOR : performs a logical inverted OR operation.
  NXOR : performs a logical inverted exclusive OR operation.

  Signals are represented with underscore and minus characters,
  an undescore matching a low level (0, or false) and a minus matching a high level (1, or true)."
  (:require [clojure.string :as str]))

(defn input []
  (doall (line-seq (java.io.BufferedReader. *in*))))

(defn output [s]
  (println s)
  (flush))

(defn deserialize
  "Returns integer parsed from binary string `s`; an underscore as 0, and hyphen as 1.

  (deserialize \"-_-_\")
  ;; => 2r1010
  "
  [s]
  (let [binary (->> s (map {\_ \0 \- \1}) (apply str))]
    (Long/parseLong binary 2)))

(defn serialize
  "Returns string generated from integer `n`; an underscore as 0, and hyphen as 1.
  If the result length is shorter than given `x`, it will be 0-padded to the left,
  otherwise returns the last `x` value.

  (serialize 2r1010 26)
  ;; => \"______________________-_-_\"

  (serialize 2r1101 3)
  ;; => \"-_-\"
  "
  [n x]
  (let [s (-> (format (str "%" x "s") (Long/toBinaryString n))
              (str/replace #"\s" "0"))]
    (->> s (take-last x) (map {\0 \_ \1 \-}) (apply str))))

(defn parse-data
  "Returns a map:
     key as a first word of a line, val as an parsed binary.

  (parse-data [\"DATA1 -_\" \"DATA2 --\"])
  ;; => {\"DATA1\" 2r10, \"DATA2\" 2r11}
  "
  [lines]
  (->> lines
       (map #(str/split % #"\s"))
       (reduce (fn [m [k bits]] (assoc m k (deserialize bits))) {})))

(defn parse
  "Returns an information map which is constructed from input sequence.

  (parse [\"2\"
          \"3\"
          \"A _\"
          \"B -\"
          \"C AND A B\"
          \"D OR A B\"
          \"E XOR A B\"])
  ;; => {:data {\"A\" 0, \"B\" 1}
  ;; =>  :len  1
  ;; =>  :ops  [{:name \"C\" :op [\"AND\" \"A\" \"B\"]}
  ;; =>         {:name \"D\" :op [\"OR\"  \"A\" \"B\"]}
  ;; =>         {:name \"E\" :op [\"XOR\" \"A\" \"B\"]}]}
  "
  [coll]
  (let [[data-n op-n] (->> coll (take 2) (map #(Integer. %)))
        lines         (drop 2 coll)
        data          (take data-n lines)
        ops           (->> lines (drop data-n) (take op-n))]
    {:data (parse-data data)
     :len  (-> data first (str/split #"\s") second count)
     :ops  (->> ops
                (map #(str/split % #"\s"))
                (map (fn [[k & op-args]] {:name k :op op-args})))}))

(defn calc
  "Performs a logical calculation `op` with `args-ref`,
  which refers an identity stored in `data` map.

  (calc {:a 2r1010, :b 2r0101} [\"AND\" :a :b])
  ;; => 0
  "
  [data [op & args-ref]]
  (let [args (map data args-ref)
        f    (case op
               "AND"  bit-and
               "OR"   bit-or
               "XOR"  bit-xor
               "NAND" (comp bit-not bit-and)
               "NOR"  (comp bit-not bit-or)
               "NXOR" (comp bit-not bit-xor))]
    (apply f args)))

(defn perform
  "Performs logical calculations to givem map `m`, and returns the results.

  (perform {:data {\"A\" 2r00111000111000111000111000,
                   \"B\" 2r00001110001110001110001110}
            :len 26
            :ops  [{:name \"C\" :op [\"AND\" \"A\" \"B\"]}
                   {:name \"D\" :op [\"OR\"  \"A\" \"B\"]}
                   {:name \"E\" :op [\"XOR\" \"A\" \"B\"]}]})
  ;; => [\"C ____-_____-_____-_____-___\"
  ;; =>  \"D __-----_-----_-----_-----_\"
  ;; =>  \"E __--_--_--_--_--_--_--_--_\"]
  "
  [{:keys [data ops len]}]
  (letfn [(f [{:keys [name op]}]
            (str name " " (-> (calc data op) (serialize len))))]
    (map f ops)))

(defn solve []
  (->> (input)
       parse
       perform
       (str/join "\n")
       output))
