(ns codeingame.easy.horse-racing-duals-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.horse-racing-duals :as sut]))

(deftest horse-racing-duals-test
  (testing "Specification"
    (is (match? 1
                (sut/closest-difference [5 8 9 11]))))
  (testing "Run"
    (is (match? "1\n"
                (with-out-str
                  (with-in-str "3\n5\n8\n9"
                    (sut/duals)))))))
