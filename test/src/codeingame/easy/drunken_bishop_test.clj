(ns codeingame.easy.drunken-bishop-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [codingame.easy.drunken-bishop :as sut]
            [testdoc.core]))

(deftest testdoc-test
  (is (testdoc #'sut/hex->bits))
  (is (testdoc #'sut/bits->pos))
  (is (testdoc #'sut/input))
  (is (testdoc #'sut/coords-seq)))

(deftest drunken-bishop-test
  (testing "Parse"
    (let [hex      [0xfc 0x94]
          expected [2r00 2r11 2r11 2r11, 2r00 2r01 2r01 2r10]]
      (is (match? expected (sut/hex->bits hex)))))

  (testing "Solve"
    (let [p        '(252 148 176 193 229 176 152 124 88 67 153 118 151 238 159 183)
          expected ["+---[CODINGAME]---+"
                    "|       .=o.  .   |"
                    "|     . *+*. o    |"
                    "|      =.*..o     |"
                    "|       o + ..    |"
                    "|        S o.     |"
                    "|         o  .    |"
                    "|          .  . . |"
                    "|              o .|"
                    "|               E.|"
                    "+-----------------+"]]
      (is (match? expected (sut/solve p))))))
