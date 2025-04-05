(ns codeingame.easy.six-degrees-of-kevin-bacon-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [mockfn.macros :refer [providing]]
            [testdoc.core]
            [codingame.easy.six-degrees-of-kevin-bacon :as sut]))


(deftest six-degrees-of-kevin-bacon-test
  (testing "Problem"
    (testing "Example"
      (let [input "Elvis Presley
3
Change of Habit: Elvis Presley, Mary Tyler Moore, Barbara McNair, Jane Elliot, Ed Asner
JFK: Kevin Costner, Kevin Bacon, Tommy Lee Jones, Laurie Metcalf, Gary Oldman, Ed Asner
Sleepers: Kevin Bacon, Jason Patric, Brad Pitt, Robert De Niro, Dustin Hoffman"]
        (is (match? {:actor string?
                     :graph (m/pred (fn [actual]
                                      (and (map? actual)
                                           (every? string? (keys actual))
                                           (every? set? (vals actual))))
                                    "Map with string? keys and set? values.")}
                    (with-in-str input (sut/read-input))))
        (is (match? 2 (with-in-str input (-> (sut/read-input) sut/solve))))))))
