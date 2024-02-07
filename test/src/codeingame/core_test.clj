(ns codeingame.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [codingame.core :as sut]))

(deftest the-decent-test
  (testing "Takes eight numbers from stdin"
    (is (match? [1 2 3 4 5 6 7 8]
                (with-in-str "1\n2\n3\n4\n5\n6\n7\n8\n"
                  (sut/parse-integers 8)))))
  (testing "Returns index of max number"
    (is (match? 4
                (sut/max-index [0 1 2 3 10 5 6 7])))))

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
