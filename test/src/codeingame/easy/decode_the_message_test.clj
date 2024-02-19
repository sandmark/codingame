(ns codeingame.easy.decode-the-message-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.decode-the-message :as sut]
            [testdoc.core]))

(deftest testdoc-test
  (is (testdoc #'sut/solve)))

(deftest decode-the-message-test
  (testing "Solve"
    (is (match? "ja"
                (sut/solve 35 "abcdefghijklmnopqrstuvwxyz")))
    (is (match? "ja\n"
                (with-out-str
                  (with-in-str "35\nabcdefghijklmnopqrstuvwxyz"
                    (->> (sut/input) (apply sut/solve) (sut/output))))))
    (is (match? "Hello ! World\n"
                (with-out-str
                  (with-in-str "34170657950616
H_eo: Wrld!"
                    (->> (sut/input) (apply sut/solve) (sut/output))))))))
