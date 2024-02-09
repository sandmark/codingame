(ns codeingame.easy.temperatures-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.temperatures :as sut]))

(deftest temperatures-test
  (testing "Temperatures"
    (is (match? [42 5 12 21 -5 24]
                (with-in-str "6\n42 5 12 21 -5 24"
                  (sut/temperatures-input))))
    (is (match? []
                (with-in-str "0\n"
                  (sut/temperatures-input))))
    (is (match? 0
                (sut/find-closest-zero [])))
    (is (match? 1
                (sut/find-closest-zero [-1 1 -2 2])))
    (is (match? 1
                (sut/find-closest-zero [3 2 1 -1])))
    (is (match? 1
                (sut/find-closest-zero [3 2 1])))
    (is (match? 5
                (sut/find-closest-zero [42 5 12 21 -5 24])))))
