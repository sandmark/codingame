(ns dev
  (:require
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.repl.deps :refer :all]
   [clojure.tools.namespace.repl :refer [refresh]]
   [orchestra.spec.test :as st]))

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test/src" "spec/src")

(defn stop []
  (st/unstrument))

(defn start []
  (st/instrument))
