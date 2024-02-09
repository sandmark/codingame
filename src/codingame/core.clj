(ns codingame.core
  (:require [clojure.string :as str]))

(defn output [msg] (println msg) (flush))
(defn debug [msg] (binding [*out* *err*] (println msg) (flush)))
