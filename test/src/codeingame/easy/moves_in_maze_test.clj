(ns codeingame.easy.moves-in-maze-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [codingame.easy.moves-in-maze :as sut]
            [testdoc.core]))

(defn lines->vec2d [ls]
  (->> ls (mapv vec)))

(deftest moves-in-maze-test
  (testing "IO"
    (let [input  "10 5
##########
#S.......#
##.#####.#
##.#.....#
##########
"
          data   {:w    10
                  :h    5
                  :maze [[\# \# \# \# \# \# \# \# \# \#]
                         [\# \S \. \. \. \. \. \. \. \#]
                         [\# \# \. \# \# \# \# \# \. \#]
                         [\# \# \. \# \. \. \. \. \. \#]
                         [\# \# \# \# \# \# \# \# \# \#]]}
          output "##########
#01234567#
##2#####8#
##3#DCBA9#
##########
"]
      (is (match? data (with-in-str input (sut/input))))
      (is (match? output (with-in-str input
                           (with-out-str
                             (-> (sut/input) sut/solve sut/output)))))))

  (testing "Utility"
    (let [p {:maze [[\# \# \#]
                    [\# 0 \#]
                    [\# 1 \#]]
             :w    3, :h 3}]
      (is (match? [[1 0 1] [1 2 1] [0 1 1] [2 1 1]]
                  (sut/neighbours p 1 1 0)))))

  (testing "Maze"
    (testing "Blocked"
      (let [p        {:w 5, :h 5, :maze (lines->vec2d ["....."
                                                       "....."
                                                       ".###."
                                                       ".#S#."
                                                       ".###."])}
            expected (lines->vec2d ["....."
                                    "....."
                                    ".###."
                                    ".#0#."
                                    ".###."])]
        (is (match? expected (sut/solve p)))))
    (testing "Basic"
      (let [p        {:w    10
                      :h    5
                      :maze (lines->vec2d ["##########"
                                           "#S.......#"
                                           "##.#####.#"
                                           "##.#.....#"
                                           "##########"])}
            expected (lines->vec2d ["##########"
                                    "#01234567#"
                                    "##2#####8#"
                                    "##3#DCBA9#"
                                    "##########"])]
        (is (match? expected (sut/solve p)))))
    (testing "Loop"
      (let [p        {:w    10
                      :h    5
                      :maze (lines->vec2d ["##########"
                                           "#S.......#"
                                           "##.#####.#"
                                           "##.......#"
                                           "##########"])}
            expected (lines->vec2d ["##########"
                                    "#01234567#"
                                    "##2#####8#"
                                    "##3456789#"
                                    "##########"])]
        (is (match? expected (sut/solve p)))))))
