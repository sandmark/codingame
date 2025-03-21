(ns codeingame.easy.nato-alphabets-odd-uncles-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [testdoc.core]
            [codingame.easy.nato-alphabets-odd-uncles :as sut]))

(deftest nato-alphabets-test
  (testing "Same year"
    (is (match? "Tango Hotel Echo Quebec Uniform India Charlie Kilo"
                (sut/solve "Tripoli Havana Edison Quebec Uppsala Italia Casablanca Kilogramme")))
    (is (match? "Casablanca Casablanca Denmark"
                (sut/solve "Charlie Charlie Duff")))
    (is (match? "Capture Capture Destroy"
                (sut/solve "Charlie Charlie Delta")))))
