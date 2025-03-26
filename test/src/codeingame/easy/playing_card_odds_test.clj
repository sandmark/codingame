(ns codeingame.easy.playing-card-odds-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [mockfn.macros :refer [providing]]
            [testdoc.core]
            [codingame.easy.playing-card-odds :as sut]))

(deftest playing-card-odds-test
  (testing "Example"
    (is (match? {:rms ["45C"] :soughts ["7H"]}
                (with-in-str "1 1\n45C\n7H" (sut/read-input))))
    (is (match? 2 (sut/solve {:rms ["45C"] :soughts ["7H"]})))
    (is (match? 32 (sut/solve {:rms ["45C"] :soughts ["7" "H"]}))))

  (testing "Problem: Impossible"
    (is (match? 0 (sut/solve {:rms ["7" "H"] :soughts ["7H" "7D" "KH"]}))))

  (testing "Problem: A lock"
    (let [rms ["CH" "23456789" "TDS"], soughts ["ADS" "JQK"]]
      (is (match? 100 (sut/solve {:rms rms :soughts soughts})))))

  (testing "Problem: Mixture"
    (let [rms     ["234C" "567H" "89TCH" "JQKD" "25JS" "369D" "A"]
          soughts ["JQKAS" "2469H" "4TCD"]]
      (is (match? 22 (sut/solve {:rms rms :soughts soughts})))))

  (testing "Problem: Decimation"
    (let [rms     ["23456789T" "CD" "ACHS" "23KH"]
          soughts ["KCS" "TQH"]]
      (is (match? 40 (sut/solve {:rms rms :soughts soughts}))))))
