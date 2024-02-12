(ns codeingame.easy.unary-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.unary :as sut]))

(deftest unary-test
  (testing "Character to unary blocks"
    (is (match? [1 0 0 0 0 1 1]
                (sut/char->binary \C)))
    (is (match? [0 1 0 0 1 0 1]
                (sut/char->binary \%)))

    (is (match? ["0" "0"]
                (sut/series->block [1])))

    (is (match? "0 0 00 0000 0 00"
                (sut/str->unary "C")))
    (is (match? "0 0 00 0000 0 000 00 0000 0 00"
                (sut/str->unary "CC")))

    (is (match? "00 0 0 0 00 00 0 0 00 0 0 0"
                (sut/str->unary "%")))))
