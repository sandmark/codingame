(ns codeingame.easy.abcdefghijklmnopqrstuvwxyz-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.abcdefghijklmnopqrstuvwxyz :as sut]
            [testdoc.core]))

(deftest alphabetical-test
  (let [example        ["10"
                        "qadnhwbnyw"
                        "iiopcygydk"
                        "bahlfiojdc"
                        "cfijtdmkgf"
                        "dzhkliplzg"
                        "efgrmpqryx"
                        "loehnovstw"
                        "jrsacymeuv"
                        "fpnocpdkrs"
                        "jlmsvwvuih"]
        example-answer "----------
----------
ba--------
c-ij------
d-hkl---z-
efg-mpqryx
----no-stw
--------uv
----------
----------"
        example-parsed {:len   10
                        :board [[\q \a \d \n \h \w \b \n \y \w]
                                [\i \i \o \p \c \y \g \y \d \k]
                                [\b \a \h \l \f \i \o \j \d \c]
                                [\c \f \i \j \t \d \m \k \g \f]
                                [\d \z \h \k \l \i \p \l \z \g]
                                [\e \f \g \r \m \p \q \r \y \x]
                                [\l \o \e \h \n \o \v \s \t \w]
                                [\j \r \s \a \c \y \m \e \u \v]
                                [\f \p \n \o \c \p \d \k \r \s]
                                [\j \l \m \s \v \w \v \u \i \h]]}
        example-route  [[2 1] [2 0] [3 0] [4 0] [5 0] [5 1] [5 2] [4 2] [3 2] [3 3] [4 3] [4 4] [5 4] [6 4] [6 5] [5 5] [5 6] [5 7] [6 7] [6 8] [7 8] [7 9] [6 9] [5 9] [5 8] [4 8]]]

    (testing "Structure"
      (is (match? example-parsed
                  (sut/parse example))))
    (testing "find-char"
      (is (match? false (sut/find-char (:board example-parsed) 0 0 \a)))
      (is (match? true (sut/find-char (:board example-parsed) 0 1 \a))))

    (testing "solve-path"
      (is (match? example-route
                  (sut/solve-path (:board example-parsed) 2 1))))

    (testing "solve"
      (is (match? example-answer
                  (sut/solve example-parsed))))))
