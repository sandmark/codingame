(ns codingame.easy.periodic-table-spelling
  (:require [clojure.string :as str]
            [tupelo.core :as t]))

(def periodic-elements
  #{"H" "He" "Li" "Be" "B" "C" "N" "O" "F" "Ne" "Na" "Mg" "Al" "Si" "P" "S" "Cl" "Ar" "K" "Ca" "Sc" "Ti" "V" "Cr" "Mn" "Fe" "Co" "Ni" "Cu" "Zn" "Ga" "Ge" "As" "Se" "Br" "Kr" "Rb" "Sr" "Y" "Zr" "Nb" "Mo" "Tc" "Ru" "Rh" "Pd" "Ag" "Cd" "In" "Sn" "Sb" "Te" "I" "Xe" "Cs" "Ba" "La" "Ce" "Pr" "Nd" "Pm" "Sm" "Eu" "Gd" "Tb" "Dy" "Ho" "Er" "Tm" "Yb" "Lu" "Hf" "Ta" "W" "Re" "Os" "Ir" "Pt" "Au" "Hg" "Tl" "Pb" "Bi" "Po" "At" "Rn" "Fr" "Ra" "Ac" "Th" "Pa" "U" "Np" "Pu" "Am" "Cm" "Bk" "Cf" "Es" "Fm" "Md" "No" "Lr" "Rf" "Db" "Sg" "Bh" "Hs" "Mt" "Ds" "Rg" "Cn" "Nh" "Fl" "Mc" "Lv" "Ts" "Og"})

(defn split-word [n word]
  (if-not (< (count word) n)
    [(subs word 0 n) (str/capitalize (subs word n))]
    ["" ""]))

(defn candidates [{:keys [remaining spelling]}]
  (->> [1 2]
       (map #(split-word % remaining))
       (keep (fn [[e r]] (when (contains? periodic-elements e)
                          {:element e :remaining r :spelling (str spelling e)})))))

(defn search [[q v results]]
  (let [word (peek q), q' (pop q), cds (candidates word)]
    (cond (contains? v word)         [q' v results]
          (empty? (:remaining word)) [q' (conj v word) (conj results word)]
          :else                      [(apply conj q' cds) (conj v word) results])))

(defn terminated? [[q]]
  (empty? q))

(defn bfs [start]
  (->> [(apply conj clojure.lang.PersistentQueue/EMPTY start) #{} []]
       (iterate search)
       (drop-while (comp not terminated?))
       first))

(defn solve [word]
  (let [[_ _ result] (bfs (candidates {:remaining word}))]
    (if (empty? result)
      ["none"]
      (->> result (map :spelling) sort))))

(defn read-input []
  (-> *in* java.io.BufferedReader. line-seq first))

(defn output [words]
  (doseq [word words]
    (println word)))

(comment
  (defn -main [& _]
    (-> (read-input) solve output)))
