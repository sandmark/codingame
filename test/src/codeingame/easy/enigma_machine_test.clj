(ns codeingame.easy.enigma-machine-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.enigma-machine :as sut]
            [clojure.string :as str]))

(deftest enigma-machine-test
  (testing "Util"
    (is (match? 0 (sut/nth-cycle [0 1 2 3 4] 10)))
    (is (match? 4 (sut/nth-cycle [0 1 2 3 4] -1)))
    (is (match? 3 (sut/nth-cycle [0 1 2 3 4] -2))))
  (testing "Caesar"
    (is (match? "ABC" (sut/caesar-shift 0 "AAA")))
    (is (match? "ZAB" (sut/caesar-shift 0 "ZZZ")))
    (is (match? "EFG" (sut/caesar-shift 4 "AAA")))
    (is (match? "DEF" (sut/caesar-shift 4 "ZZZ")))
    (is (match? "AAA" (sut/caesar-unshift 4 "EFG")))
    (is (match? "ZYX" (sut/caesar-unshift 1 "AAA")))
    (is (match? "E"   (sut/caesar-unshift 9 "N")))
    (is (match? "THEQUICKBROWNFOXJUMPSOVERALAZYSPHINXOFBLACKQUARTZ"
                (sut/caesar-unshift 5 "YNLYDSNWOFDMEXHREQJNROWGUEQGGGBZSUALDVSDTWFMRYQTA"))))

  (testing "Rotor"
    (is (match? {\E \J \F \L \G \C}
                (sut/make-rotor "BDFHJLCPRTXVZNYEIWGAKMUSQO")))
    (is (match? "JLC"
                (sut/encode [(sut/make-rotor "BDFHJLCPRTXVZNYEIWGAKMUSQO")] "EFG")))
    (is (match? "BHD"
                (sut/encode [(sut/make-rotor "AJDKSIRUXBLHWTMCQGZNPYFVOE")] "JLC")))
    (is (match? "KQF"
                (sut/encode [(sut/make-rotor "EKMFLGDQVZNTOWYHXUSPAIBRCJ")] "BHD")))
    (is (match? "BHD"
                (sut/decode [(sut/make-rotor  "EKMFLGDQVZNTOWYHXUSPAIBRCJ")] "KQF"))))

  (testing "Encode/Decode"
    (is (match? "KQF"
                (apply sut/enigma (str/split-lines "ENCODE
4
BDFHJLCPRTXVZNYEIWGAKMUSQO
AJDKSIRUXBLHWTMCQGZNPYFVOE
EKMFLGDQVZNTOWYHXUSPAIBRCJ
AAA"))))

    (is (match? "AAA"
                (apply sut/enigma (str/split-lines "DECODE
4
BDFHJLCPRTXVZNYEIWGAKMUSQO
AJDKSIRUXBLHWTMCQGZNPYFVOE
EKMFLGDQVZNTOWYHXUSPAIBRCJ
KQF"))))

    (is (match? "PQSACVVTOISXFXCIAMQEM"
                (apply sut/enigma (str/split-lines "ENCODE
9
BDFHJLCPRTXVZNYEIWGAKMUSQO
AJDKSIRUXBLHWTMCQGZNPYFVOE
EKMFLGDQVZNTOWYHXUSPAIBRCJ
EVERYONEISWELCOMEHERE"))))

    (is (match? "EVERYONEISWELCOMEHERE"
                (apply sut/enigma (str/split-lines "DECODE
9
BDFHJLCPRTXVZNYEIWGAKMUSQO
AJDKSIRUXBLHWTMCQGZNPYFVOE
EKMFLGDQVZNTOWYHXUSPAIBRCJ
PQSACVVTOISXFXCIAMQEM"))))

    (is (match? "THEQUICKBROWNFOXJUMPSOVERALAZYSPHINXOFBLACKQUARTZ"
                (apply sut/enigma (str/split-lines "DECODE
5
BDFHJLCPRTXVZNYEIWGAKMUSQO
AJDKSIRUXBLHWTMCQGZNPYFVOE
EKMFLGDQVZNTOWYHXUSPAIBRCJ
XPCXAUPHYQALKJMGKRWPGYHFTKRFFFNOUTZCABUAEHQLGXREZ"))))))
