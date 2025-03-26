(ns codeingame.easy.turn-here-sign-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [testdoc.core]
            [codingame.easy.turn-here-sign :as sut]))

(deftest docstring-tests
  (is (testdoc #'sut/create-part))
  (is (testdoc #'sut/create-line))
  (is (testdoc #'sut/indent-levels))
  (is (testdoc #'sut/indent-line)))

(deftest turn-here-sign-test
  (testing "IO"
    (let [input "right 3 9 8 6 2"]
      (is (match? {:direction \> :howmany 3 :height 9
                   :thickness 8  :spacing 6 :indent 2}
                  (with-in-str input (sut/read-input))))))
  (testing "Format"
    (let [m {:direction \> :howmany 3 :height 9
             :thickness 8  :spacing 6 :indent 2}]
      (is (match? ">>>>>>>>      >>>>>>>>      >>>>>>>>
  >>>>>>>>      >>>>>>>>      >>>>>>>>
    >>>>>>>>      >>>>>>>>      >>>>>>>>
      >>>>>>>>      >>>>>>>>      >>>>>>>>
        >>>>>>>>      >>>>>>>>      >>>>>>>>
      >>>>>>>>      >>>>>>>>      >>>>>>>>
    >>>>>>>>      >>>>>>>>      >>>>>>>>
  >>>>>>>>      >>>>>>>>      >>>>>>>>
>>>>>>>>      >>>>>>>>      >>>>>>>>"
                  (sut/render m))))))
