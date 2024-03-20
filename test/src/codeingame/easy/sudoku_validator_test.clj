(ns codeingame.easy.sudoku-validator-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.sudoku-validator :as sut]
            [testdoc.core]))

(deftest sudoku-validator
  (let [;; Input
        input "1 2 3 4 5 6 7 8 9
4 5 6 7 8 9 1 2 3
7 8 9 1 2 3 4 5 6
9 1 2 3 4 5 6 7 8
3 4 5 6 7 8 9 1 2
6 7 8 9 1 2 3 4 5
8 9 1 2 3 4 5 6 7
2 3 4 5 6 7 8 9 1
5 6 7 8 9 1 2 3 4
"

        ;; Correct
        p1 [[1 2 3 4 5 6 7 8 9]
            [4 5 6 7 8 9 1 2 3]
            [7 8 9 1 2 3 4 5 6]
            [9 1 2 3 4 5 6 7 8]
            [3 4 5 6 7 8 9 1 2]
            [6 7 8 9 1 2 3 4 5]
            [8 9 1 2 3 4 5 6 7]
            [2 3 4 5 6 7 8 9 1]
            [5 6 7 8 9 1 2 3 4]]

        ;; Error: Sub Grid
        p2 [[4 3 5 2 6 9 7 8 1]
            [6 8 2 5 7 1 4 9 3]
            [8 9 7 1 3 4 5 6 2]
            [1 2 6 8 9 5 3 4 7]
            [3 7 4 6 8 2 9 1 5]
            [9 5 1 7 4 3 6 2 8]
            [5 1 9 3 2 6 8 7 4]
            [2 4 8 9 5 7 1 3 6]
            [7 6 3 4 1 8 2 5 9]]

        ;; Error: Row
        p3 [[4 3 2 2 6 9 7 8 1]
            [6 8 5 5 7 1 4 9 3]
            [1 9 7 8 3 4 5 6 2]
            [8 2 6 1 9 5 3 4 7]
            [3 7 4 6 8 2 9 1 5]
            [9 5 1 7 4 3 6 2 8]
            [5 1 9 3 2 6 8 7 4]
            [2 4 8 9 5 7 1 3 6]
            [7 6 3 4 1 8 2 5 9]]

        ;; Error: not enough summing
        p4 [[1 3 3 4 5 6 7 7 9]
            [4 5 6 7 7 9 1 3 3]
            [7 7 9 1 3 3 4 5 6]
            [9 1 3 3 4 5 6 7 7]
            [3 4 5 6 7 7 9 1 3]
            [6 7 7 9 1 3 3 4 5]
            [7 9 1 3 3 4 5 6 7]
            [3 3 4 5 6 7 7 9 1]
            [5 6 7 7 9 1 3 3 4]]]

    (testing "IO"
      (is (match? p1 (with-in-str input (sut/input))))
      (is (match? "true\n"
                  (with-in-str input
                    (with-out-str
                      (-> (sut/input) sut/valid? sut/output))))))

    (testing "Validation"
      (is (match? true (sut/valid? p1)))
      (is (match? false (sut/valid? p2)))
      (is (match? false (sut/valid? p3)))
      (is (match? false (sut/valid? p4))))))
