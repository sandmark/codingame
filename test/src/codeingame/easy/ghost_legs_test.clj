(ns codeingame.easy.ghost-legs-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.ghost-legs :as sut]
            [clojure.string :as str]))

(deftest ghost-legs-test
  (let [route-vec ["|  |  |" "|--|  |" "|  |--|" "|  |--|" "|  |  |"]]
    (testing "Parse"
      (testing "Route"
        (testing "Direction"
          (is (= identity (sut/->dir [\space \| \space])))
          (is (= dec      (sut/->dir [\- \| \space])))
          (is (= inc      (sut/->dir [\space \| \-]))))

        (is (= [[identity identity identity]
                [inc dec identity]
                [identity inc dec]
                [identity inc dec]
                [identity identity identity]]
               (sut/parse-routes route-vec)))))

    (testing "Search"
      (let [route (sut/parse-routes route-vec)]
        (testing "Entry"
          (is (match? 1 (sut/search-entry 0 route)))
          (is (match? 0 (sut/search-entry 1 route)))
          (is (match? 2 (sut/search-entry 2 route))))

        (is (match? [["A" "2"] ["B" "1"] ["C" "3"]]
                    (sut/search ["A" "B" "C"] ["1" "2" "3"] route))))))
  (testing "Solve"
    (let [problem "7 7
A  B  C
|  |  |
|--|  |
|  |--|
|  |--|
|  |  |
1  2  3"]
      (is (match? "A2\nB1\nC3" (sut/solve (str/split-lines problem))))
      (is (match? "A2\nB1\nC3\n"
                  (with-out-str
                    (with-in-str problem
                      (-> (sut/input) (sut/solve) (sut/output)))))))))
