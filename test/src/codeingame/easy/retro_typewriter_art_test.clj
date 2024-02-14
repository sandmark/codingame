(ns codeingame.easy.retro-typewriter-art-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.retro-typewriter-art :as sut]))

(deftest retro-typewriter-art-test
  (testing "Specification"
    (testing "Chunk"
      (testing "Character"
        (is (match? "zzzz"         (sut/expand "4z")))
        (is (match? "{"            (sut/expand "1{")))
        (is (match? "=========="   (sut/expand "10="))))
      (testing "Abbreviation"
        (is (match? "\\\\\\\\\\"   (sut/expand "5bS")))
        (is (match? "\n"           (sut/expand "nl")))
        (is (match? "\n\n"         (sut/expand "2nl")))
        (is (match? "''"           (sut/expand "2sQ")))
        (is (match? "   "          (sut/expand "3sp"))))
      (testing "Numeric"
        (is (match? "77"           (sut/expand "27")))
        (is (match? "333333333333" (sut/expand "123")))))

    (testing "Recipe"
      (testing "SmallCat"
        (is (match?
             " /\\_/\\\n( o.o )\n > ^ <\n  |||"
             (sut/render "1sp 1/ 1bS 1_ 1/ 1bS nl 1( 1sp 1o 1. 1o 1sp 1) nl 1sp 1> 1sp 1^ 1sp 1< nl 2sp 3|")))))))
