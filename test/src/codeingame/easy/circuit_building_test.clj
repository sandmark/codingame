(ns codeingame.easy.circuit-building-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [codingame.easy.circuit-building :as sut]
            [testdoc.core]
            [clojure.string :as str]))

(deftest circuit-building-test
  (testing "Parser"
    (are [s expected] (match? expected (sut/parse {'A 20 'B 10 'C 30} s))
      "( A B )"             '(20 10)
      "[ ( A B ) [ C A ] ]" ['(20 10) [30 20]])

    (testing "Case: Complex"
      (let [s "( Alfa [ Charlie Delta ( Bravo [ Echo ( Foxtrot Golf ) ] ) ] )"
            m {'Alfa 1, 'Bravo 1, 'Charlie 12, 'Delta 4, 'Echo 2, 'Foxtrot 10, 'Golf 8}]
        (is (= '(1 [12 4 (1 [2 (10 8)])])
               (sut/parse m s))))))

  (testing "Resistance"
    (is (match? 32/3 (sut/calc ['(24 8) [48 24]])))
    (is (match? "30.0" (sut/solve ["A 20" "B 10"] "( A B )"))))

  (testing "IO"
    (is (match? [["A 20" "B 10"] "( A B )"]
                (with-in-str "2\nA 20\nB 10\n( A B )\n"
                  (sut/input))))

    (testing "Case: Complex"
      (let [p        "7
Alfa 1
Bravo 1
Charlie 12
Delta 4
Echo 2
Foxtrot 10
Golf 8
( Alfa [ Charlie Delta ( Bravo [ Echo ( Foxtrot Golf ) ] ) ] )
"
            expected "2.4"]
        (is (match? expected (with-in-str p (->> (sut/input) (apply sut/solve)))))))))
