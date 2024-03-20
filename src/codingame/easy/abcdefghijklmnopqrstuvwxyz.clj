(ns codingame.easy.abcdefghijklmnopqrstuvwxyz
  "This program problem find out the consecutive string from a to z in alphabetical order
  in a multi-line string m of n lines of length n.
  You can move up, right, left or down. First, you find the a.
  if there is a b either up, down, left or right from the position of a, you can move there.
  if there is a c either up, down, left or right from the position of b, you can move there.
  continues below to z.
  Rewrites all nonconsecutive strings of letters a through z to -.
  In other words, this problem only displays the consecutive string from
  a to z in a multi-line string m of n lines of length n.
  Answer to output , as follows.

  Example:


  10
  qadnhwbnyw
  iiopcygydk
  bahlfiojdc
  cfijtdmkgf
  dzhkliplzg
  efgrmpqryx
  loehnovstw
  jrsacymeuv
  fpnocpdkrs
  jlmsvwvuih


  The answer to this is...


  ----------
  ----------
  ba--------
  c-ij------
  d-hkl---z-
  efg-mpqryx
  ----no-stw
  --------uv
  ----------
  ----------


  As above, only the string alphabetically from a to z should be displayed,
  and the other parts should be -."
  (:require [clojure.string :as str]))

(defn parse [[n-str & lines]]
  {:len (Integer. n-str), :board (mapv vec lines)})

(defn find-char [board x y target]
  (some-> board (get-in [x y] false) (= target)))

(defn solve-path [board start-x start-y]
  (loop [[current :as chars] (list \a), [[x y] :as path] (list [start-x start-y])]
    (let [target (-> current int inc char)]
      (cond (= \z current)                     (->> path reverse (into []))
            (find-char board (inc x) y target) (recur (conj chars target) (conj path [(inc x) y]))     ;; Down
            (find-char board (dec x) y target) (recur (conj chars target) (conj path [(dec x) y]))     ;; Up
            (find-char board x (inc y) target) (recur (conj chars target) (conj path [x (inc y)]))     ;; Right
            (find-char board x (dec y) target) (recur (conj chars target) (conj path [x (dec y)])))))) ;; Left

(defn solve [{:keys [len board]}]
  (let [entry  (first (remove nil? (for [x (range len), y (range len) :when (= \a (get-in board [x y]))]
                                     (solve-path board x y))))
        path   (map-indexed #(vector (-> \a int (+ %1) char) %2) entry)
        answer (vec (repeat len (vec (repeat len \-))))]
    (->> path
         (reduce (fn [m [alphabet [x y]]] (assoc-in m [x y] alphabet)) answer)
         (map (partial apply str))
         (str/join "\n"))))

(defn output [s]
  (println s)
  (flush))

(defn input []
  (-> *in* java.io.BufferedReader. line-seq))
