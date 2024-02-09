(ns codeingame.easy.power-of-thor-ep1-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [codingame.easy.power-of-thor-ep1 :as sut]
   [matcher-combinators.clj-test]))

(deftest power-of-thor-ep1-test
  (testing "Read input"
    (is (match? [[1 2] [3 4]]
                (with-in-str "1 2 3 4"
                  (sut/thor-get-input)))))
  (testing "Compare position"
    (is (match? [-1 1]
                (sut/compare-pos [0 2] [1 1])))
    (is (match? "NW"
                (sut/thor-direction [1 1] [0 0])))))
