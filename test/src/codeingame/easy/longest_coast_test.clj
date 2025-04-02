(ns codeingame.easy.longest-coast-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [mockfn.macros :refer [providing]]
            [testdoc.core]
            [codingame.easy.longest-coast :as sut]))

(deftest longest-cost-test
  (testing "Problem"
    (testing "Example"
      (let [input "3\n~~#\n~~#\n~~#"
            data  {:visited #{} :island-index 0 :size 3 :islands {} :grid [[\~ \~ \#] [\~ \~ \#] [\~ \~ \#]]}]
        (is (match? (m/in-any-order [[1 0] [0 1]])
                    (sut/neighbours [0 0] {:grid [[0 0 0] [0 0 0] [0 0 0]]})))
        (is (match? (m/in-any-order [[2 1] [0 1] [1 2] [1 0]])
                    (sut/neighbours [1 1] {:grid [[0 0 0] [0 0 0] [0 0 0]]})))
        (is (match? [[\~ \~ \#] [\~ \~ \#] [\~ \~ \#]]
                    (sut/parse-grid ["~~#" "~~#" "~~#"])))
        (is (match? {:size 3, :grid [[\~ \~ \#] [\~ \~ \#] [\~ \~ \#]]}
                    (with-in-str input (sut/read-input))))

        (is (match? {:islands {0 {:coast 3}}}
                    (sut/bfs [0 2] data)))
        (is (match? {:islands {0 {:coast 3}}}
                    (sut/solve data)))))

    (testing "Multiple Islands"
      (let [input "6\n##~~#~\n##~~~~\n~~~~#~\n~~~##~\n~####~\n~~~~~~"]
        (is (match? {:islands {0 {:coast 4}, 1 {:coast 3}, 2 {:coast 12}}}
                    (sut/solve (with-in-str input (sut/read-input)))))
        ))))
