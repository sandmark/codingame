(ns codeingame.easy.crop-circles-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [codingame.easy.crop-circles :as sut]
            [testdoc.core]))

(deftest crop-circles-test
  (testing "Instruction"
    (is (match? {:cmd :mow, :x 5 :y 6 :size 9}
                (sut/parse-instruction "fg9")))
    (is (match? {:cmd :plant-mow}
                (sut/parse-instruction "PLANTMOWjm13")))
    (is (match? {:cmd :plant}
                (sut/parse-instruction "PLANTgg7"))))

  (testing "Circle"
    (is (match? [[0 0] [0 1] [0 2] [1 0] [1 1] [1 2] [1 3] [2 0] [2 1] [2 2] [3 1]]
                (sut/circle-indices 1 1 4))))

  (testing "IO"
    (testing "Case 1"
      (let [input  "fg9 ls11 oe7"
            output "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}
{}{}{}{}{}{}{}{}{}{}{}{}{}      {}{}{}
{}{}{}          {}{}{}{}          {}{}
{}{}              {}{}              {}
{}                  {}              {}
{}                  {}              {}
{}                  {}{}          {}{}
{}                  {}{}{}      {}{}{}
{}                  {}{}{}{}{}{}{}{}{}
{}{}              {}{}{}{}{}{}{}{}{}{}
{}{}{}          {}{}{}{}{}{}{}{}{}{}{}
{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}
{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}
{}{}{}{}{}{}{}{}{}          {}{}{}{}{}
{}{}{}{}{}{}{}{}              {}{}{}{}
{}{}{}{}{}{}{}                  {}{}{}
{}{}{}{}{}{}                      {}{}
{}{}{}{}{}{}                      {}{}
{}{}{}{}{}{}                      {}{}
{}{}{}{}{}{}                      {}{}
{}{}{}{}{}{}                      {}{}
{}{}{}{}{}{}{}                  {}{}{}
{}{}{}{}{}{}{}{}              {}{}{}{}
{}{}{}{}{}{}{}{}{}          {}{}{}{}{}
{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}
"]
        (is (match? output
                    (with-in-str input
                      (with-out-str
                        (-> (sut/input) sut/solve sut/output)))))))))
