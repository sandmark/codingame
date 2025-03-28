(ns codeingame.easy.periodic-table-spelling-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [mockfn.macros :refer [providing]]
            [testdoc.core]
            [codingame.easy.periodic-table-spelling :as sut]))

(deftest periodic-table-spelling-test
  (testing "Utilities"
    (is (match? ["Ca" "Rbon"] (sut/split-word 2 "Carbon")))
    (is (match? ["N" ""] (sut/split-word 1 "N")))
    (is (match? ["" ""] (sut/split-word 1 "")))
    (is (match? (m/in-any-order [{:element "Ca" :remaining "Rbon" :spelling "Ca"}
                                 {:element "C" :remaining "Arbon" :spelling "C"}])
                (sut/candidates {:remaining "Carbon" :spelling ""})))
    (is (match? (m/in-any-order [{:element "Ar" :remaining "Bon" :spelling "CAr"}])
                (sut/candidates {:remaining "Arbon" :spelling "C"})))
    (is (match? [{:remaining "" :spelling "CArBON"}]
                (sut/candidates {:remaining "N" :spelling "CArBO"}))))

  (testing "Search"
    (is (match? true (sut/terminated? [[]])))
    (is (match? false (sut/terminated? [[{:remaining "N"}]]))))

  (testing "Problem"
    (testing "Carbon"
      (is (match? ["CArBON" "CaRbON"] (sut/solve "Carbon")))
      (is (match? ["CArBON" "CaRbON"] (with-in-str "Carbon" (sut/solve (sut/read-input))))))
    (testing "Canada"
      (is (match? ["none"] (sut/solve "Canada"))))
    (testing "Hyperpathogenesis"
      (is (match? (m/in-any-order ["HYPErPAtHOGeNEsIS" "HYPErPAtHOGeNeSIS" "HYPErPAtHOGeNeSiS" "HYPErPAtHoGeNEsIS" "HYPErPAtHoGeNeSIS" "HYPErPAtHoGeNeSiS" "HYPErPaThOGeNEsIS" "HYPErPaThOGeNeSIS" "HYPErPaThOGeNeSiS"])
                  (with-in-str "Hyperpathogenesis" (sut/solve (sut/read-input))))))))
