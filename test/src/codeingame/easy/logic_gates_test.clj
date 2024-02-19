(ns codeingame.easy.logic-gates-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.logic-gates :as sut]
            [testdoc.core]))

(deftest logic-gates-docstring-test
  (is (testdoc #'sut/calc))
  (is (testdoc #'sut/deserialize))
  (is (testdoc #'sut/serialize))
  (is (testdoc #'sut/parse-data))
  (is (testdoc #'sut/parse))
  (is (testdoc #'sut/perform)))

(deftest logic-gates-test
  (testing "Operation"
    (is (match? ["C ____-_____-_____-_____-___"
                 "D __-----_-----_-----_-----_"
                 "E __--_--_--_--_--_--_--_--_"]
                (sut/perform {:len  26
                              :data {"A" 2r00111000111000111000111000
                                     "B" 2r00001110001110001110001110}
                              :ops  [{:name "C" :op ["AND" "A" "B"]}
                                     {:name "D" :op ["OR"  "A" "B"]}
                                     {:name "E" :op ["XOR" "A" "B"]}]})))
    (is (match? ["B --___---___---___---___---"]
                (sut/perform (sut/parse ["1"
                                         "1"
                                         "A __---___---___---___---___"
                                         "B NAND A A"])))))

  (testing "Parse"
    (testing "Bits"
      (is (match? 2r00111000111000111000111000
                  (sut/deserialize "__---___---___---___---___")))
      (is (match? 2r111100001111000011110000111100001111000011
                  (sut/deserialize "----____----____----____----____----____--")))
      (is (match? "----____----____----____----____----____--"
                  (sut/serialize 2r111100001111000011110000111100001111000011 42))))
    (is (match? {:len  26
                 :data {"A" 2r00111000111000111000111000
                        "B" 2r00001110001110001110001110}
                 :ops  [{:name "C" :op ["AND" "A" "B"]}
                        {:name "D" :op ["OR"  "A" "B"]}
                        {:name "E" :op ["XOR" "A" "B"]}]}
                (sut/parse ["2"
                            "3"
                            "A __---___---___---___---___"
                            "B ____---___---___---___---_"
                            "C AND A B"
                            "D OR A B"
                            "E XOR A B"])))))
