(ns codeingame.easy.the-other-side-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [mockfn.macros :refer [providing]]
            [testdoc.core]
            [codingame.easy.the-other-side :as sut]))

(deftest the-other-side-test
  (testing "Problem"
    (testing "Example"
      (let [input "2
3
+ + +
+ + +"]
        (is (match? 2 (with-in-str input (-> (sut/read-input) sut/solve))))))

    (testing "Nowhere to Stand"
      (let [input "2
2
# #
# #"]
        (is (match? 0 (with-in-str input (-> (sut/read-input) sut/solve))))))

    (testing "Stalemate"
      (let [input "3
10
# # # # # # # # # +
+ + + + + + + + # +
# # # # # # # # # +"]
        (is (match? 0 (with-in-str input (-> (sut/read-input) sut/solve))))))))
