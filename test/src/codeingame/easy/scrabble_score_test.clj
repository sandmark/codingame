(ns codeingame.easy.scrabble-score-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [matcher-combinators.matchers :as m]
            [codingame.easy.scrabble-score :as sut]
            [testdoc.core]
            [clojure.string :as str]
            [orchestra.spec.test :as st]))

(defn inst [f] (st/instrument) (f))

(clojure.test/use-fixtures :once inst)

(deftest docstring-test
  (is (testdoc #'sut/mapv-2d))
  (is (testdoc #'sut/parse-tiles))
  (is (testdoc #'sut/parse-cells))
  (is (testdoc #'sut/put-op-tiles))
  (is (testdoc #'sut/rotate))
  (is (testdoc #'sut/find-my-words-linear))
  (is (testdoc #'sut/find-my-words)))

(deftest scrabble-score-tests
  (testing "Parsing"
    (is (match? [[{:letter nil?}]]
                (sut/put-op-tiles {:tiles {} :cells (sut/parse-cells ["."])} ["."])))

    (let [input "27\nA 1\nB 3\nC 3\nD 2\nE 1\nF 4\nG 2\nH 4\nI 1\nJ 8\nK 5\nL 1\nM 3\nN 1\nO 1\nP 3\nQ 8\nR 1\nS 1\nT 1\nU 1\nV 4\nW 4\nX 8\nY 4\nZ 10\n_ 0\n4 4\nW..w\n....\n....\nl..L\n....\n.OR.\n....\n....\n....\n.OR.\n.F..\n....\n"]
      (is (match? {:tiles {\A 1, \B 3, \C 3, \D 2, \E 1, \F 4, \G 2, \H 4, \I 1, \J 8, \K 5, \L 1, \M 3, \N 1, \O 1, \P 3, \Q 8, \R 1, \S 1, \T 1, \U 1, \V 4, \W 4, \X 8, \Y 4, \Z 10, \_ 0}}
                  (with-in-str input (sut/read-input))))
      (is (match? {:cells [[{:multiplier :triple-word} {} {} {:multiplier :double-word}]
                           [{} {} {} {}]
                           [{} {} {} {}]
                           [{:multiplier :double-letter} {} {} {:multiplier :triple-letter}]]}
                  (with-in-str input (sut/read-input))))
      (is (match? {:op-board [[{} {} {} {}]
                              [{} {:letter \O, :score 1} {:letter \R, :score 1} {}]
                              [{} {} {} {}]
                              [{} {} {} {}]]}
                  (with-in-str input (sut/read-input))))
      (is (match? {:my-board [[{} {} {} {}]
                              [{} {:letter \O, :score 1} {:letter \R, :score 1} {}]
                              [{} {:letter \F, :score 4, :mine true} {} {}]
                              [{} {} {} {}]]}
                  (with-in-str input (sut/read-input))))))

  (testing "Calculation"
    (is (match? {:word "OF" :score 5}
                (sut/cells->word [{:multiplier nil, :letter \O, :score 1} {:multiplier nil, :letter \F, :score 4, :mine true}])))
    (is (match? {:word "ON" :score 10}
                (sut/cells->word [{:letter \O :score 1 :multiplier :double-word} {:letter \N :score 4}])))
    (is (match? {:word "OR" :score 30}
                (sut/cells->word [{:letter \O :score 1 :multiplier :double-word} {:letter \R :score 4 :multiplier :triple-word}]))))

  (testing "Putting"
    (let [tiles {\A 1 \O 4 \F 1}]
      (testing "Letter Score"
        (is (match? 1 (sut/calc-letter-score tiles nil \A)))
        (is (match? 2 (sut/calc-letter-score tiles :double-letter \A)))
        (is (match? 3 (sut/calc-letter-score tiles :triple-letter \A)))
        (is (match? 0 (sut/calc-letter-score tiles nil \.))))
      (testing "My Letter"
        (is (match? {:letter \A :mine true} (sut/assoc-mine-flag {:letter \.} \A)))
        (is (match? {:letter \A}            (sut/assoc-mine-flag {:letter \A} \A))))
      (is (match? {:letter \O :score 4}
                  (sut/put-my-tile tiles {:letter \O} \O)))
      (is (match? {:letter \F :score 2}
                  (sut/put-my-tile tiles {:letter \. :multiplier :double-letter} \F)))))

  (testing "With Problem 1"
    (let [tiles    (sut/parse-tiles (str/split-lines "A 1\nB 3\nC 3\nD 2\nE 1\nF 4\nG 2\nH 4\nI 1\nJ 8\nK 5\nL 1\nM 3\nN 1\nO 1\nP 3\nQ 8\nR 1\nS 1\nT 1\nU 1\nV 4\nW 4\nX 8\nY 4\nZ 10\n_ 0"))
          cells    (sut/parse-cells (str/split-lines "W..w\n....\n....\nl..L"))
          op-board (sut/put-op-tiles {:tiles tiles :cells cells} (str/split-lines "....\n.OR.\n....\n...."))
          my-board (sut/put-my-tiles {:tiles tiles :op-board op-board} (str/split-lines "....\n.OR.\n.F..\n...."))]

      (is (match? (m/embeds [(m/embeds [{:letter \R}])])
                  op-board))
      (is (match? (m/embeds [(m/embeds [{:letter \F :mine true}])])
                  my-board))))

  (testing "With Problem 2"
    (let [input "27\nA 1\nB 3\nC 3\nD 2\nE 1\nF 4\nG 2\nH 4\nI 1\nJ 8\nK 5\nL 1\nM 3\nN 1\nO 1\nP 3\nQ 8\nR 1\nS 1\nT 1\nU 1\nV 4\nW 4\nX 8\nY 4\nZ 10\n_ 0\n4 4\nW..w\n....\n....\nl..L\n....\n.O..\n.U..\n.T..\n....\n.O..\n.U..\nIT.."
          data  (with-in-str input (sut/read-input))]
      (is (match? [[{} {} {} {}]
                   [{} {} {} {}]
                   [{} {} {} {}]
                   [{:multiplier :double-letter} {} {} {}]]
                  (sut/put-op-tiles data ["...." ".O.." ".U.." ".T.."])))
      (is (match? 2                                (sut/calc-letter-score (:tiles data) :double-letter \I)))
      (is (match? {:multiplier nil :score 2}
                  (sut/put-my-tile (:tiles data) {:multiplier :double-letter :letter \.} \I)))
      (is (match? {:words [{:word "IT" :score 3}]} (sut/solve data))))))
