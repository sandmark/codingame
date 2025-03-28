(ns codeingame.easy.island-escape-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [mockfn.macros :refer [providing]]
            [testdoc.core]
            [codingame.easy.island-escape :as sut]))

(deftest island-escape-test
  (testing "Island Escape"
    (testing "Parse"
      (is (match? {:size 3
                   :grid [[0 0 0]
                          [0 2 0]
                          [0 0 0]]}
                  (with-in-str "3\n0 0 0\n0 2 0\n0 0 0" (sut/read-input)))))
    (is (match? [1 1] (sut/start-pos 3)))
    (are [pos] (match? true (sut/ocean? pos {:size 3}))
      [0 0] [1 0] [2 0] [0 1] [2 1] [0 2] [1 2] [2 2])
    (is (match? (m/in-any-order [[0 1] [1 0] [2 1] [1 2]]) (sut/neighbours [1 1])))
    (is (match? true (sut/reachable? {:grid [[0 1 0]]} 2 [0 1])))
    (is (match? false (sut/reachable? {:grid [[0 0 0]]} 2 [0 1])))
    (are [coord] (match? true (boolean (sut/inside? {:size 3} coord)))
      [0 1] [2 1] [1 0] [1 2]))

  (testing "Problem"
    (testing "City State"
      (let [p "19\n0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n0 0 0 0 0 0 0 1 1 1 1 1 0 0 0 0 0 0 0\n0 0 0 0 0 1 1 5 5 5 5 5 1 1 0 0 0 0 0\n0 0 0 1 1 5 5 4 4 2 7 7 5 5 1 1 0 0 0\n0 0 0 1 5 3 2 2 2 2 7 7 2 3 5 1 0 0 0\n0 0 1 5 3 3 6 6 6 2 2 2 2 2 3 5 1 0 0\n0 0 1 5 2 2 6 6 6 2 2 8 8 8 3 5 1 0 0\n0 1 5 3 2 2 2 2 2 2 2 8 7 8 3 3 5 1 0\n0 1 5 2 7 7 7 2 2 3 3 8 7 8 3 3 2 1 0\n0 1 5 2 7 1 7 2 3 3 3 8 7 8 3 3 2 1 0\n0 1 5 2 7 7 7 2 5 5 2 8 8 8 3 3 2 1 0\n0 1 5 3 2 4 4 2 5 5 2 2 2 2 3 3 5 1 0\n0 0 1 5 2 4 4 2 2 2 2 2 6 6 6 5 1 0 0\n0 0 1 5 3 2 2 2 7 7 7 2 6 6 6 5 1 0 0\n0 0 0 1 5 3 2 2 7 7 7 2 2 3 5 1 0 0 0\n0 0 0 1 1 5 5 3 2 2 2 3 5 5 1 1 0 0 0\n0 0 0 0 0 1 1 5 5 5 5 5 1 1 0 0 0 0 0\n0 0 0 0 0 0 0 1 1 1 1 1 0 0 0 0 0 0 0\n0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n"]
        (is (match? "yes" (sut/solve (with-in-str p (sut/read-input)))))))))
