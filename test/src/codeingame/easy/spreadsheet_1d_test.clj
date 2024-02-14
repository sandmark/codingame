(ns codeingame.easy.spreadsheet-1d-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.spreadsheet-1d :as sut]
            [clojure.string :as str]))

(deftest spreadsheet-1d-test
  (testing "Specification"
    (testing "Parse"
      (is (match? {:idx 0 :value 1}
                  (sut/parse 0 "VALUE 1 _")))
      (is (match? {:idx 0 :op :add :args [{:value -2} {:value 3}]}
                  (sut/parse 0 "ADD -2 3")))
      (is (match? {:idx 0 :op :sub :args [{:value 3} {:value 2}]}
                  (sut/parse 0 "SUB 3 2")))
      (is (match? {:idx 0 :op :mult :args [{:value 2} {:value 3}]}
                  (sut/parse 0 "MULT 2 3")))
      (is (match? {:idx 0 :op :add :args [{:ref 0} {:value 1}]}
                  (sut/parse 0 "ADD $0 1"))))

    (testing "Resolve"
      (is (match? [4 2 2]
                  (let [coll (->> ["ADD $1 $2" "VALUE $2 _" "VALUE 2 _"]
                                  (map-indexed sut/parse))]
                    (map (sut/make-resolver coll) coll))))
      (is (match? [20 120 121]
                  (sut/resolve-cells (str/split-lines
                                      "VALUE 20 _
ADD $0 100
ADD $1 1")))))))

(deftest accounting-is-hard-test
  (is (match? [-119 -78 -200 -156 285 -281 244 1481 -281 -81 1316 566 122 -281 -730 1072 -78 -447 1163 129 548 -281 10 -1556 -41 0 -200 3 -1225 200 -1559 -119 0 122 88 -285 -153 1884 1197 -1214 -444 1245 -807 -244 -285 -481 41 -200 1762 338 1478 247 1441 0 -41 1638 -119 166 403 122 488 1150 488 122 491 203 -119 -119 -434 -3 1140 1353 0 485 366 -1756 829 -1475 -34 -444 -766 -1656 -281 -3 -566 654 1072 -849 -278 -3 -766 447 10 125 688 691 366 -81 0 -1021]
              (sut/resolve-cells (str/split-lines "SUB $47 $9
SUB 44 $59
ADD $97 $67
ADD $1 $1
SUB $57 $67
ADD $47 $97
ADD $59 $59
SUB $50 $83
SUB $3 $93
SUB $4 $74
SUB $38 $0
ADD $29 $96
SUB $46 $97
SUB $5 $98
SUB $87 $66
SUB $86 $25
SUB $1 $98
SUB $84 $56
ADD $38 $78
ADD $46 $34
ADD $5 $76
SUB $3 $93
ADD $19 $31
ADD $97 $77
VALUE $54 _
SUB $6 $6
ADD $98 $2
ADD $59 $67
SUB $36 $86
SUB $98 $26
SUB $16 $7
VALUE $67 _
ADD $11 $84
VALUE $63 _
ADD $3 $6
VALUE $44 _
SUB $68 $5
ADD $7 $58
ADD $50 $82
ADD $88 -936
ADD $43 $47
ADD $58 842
SUB $80 $46
SUB $33 $96
SUB $43 $46
ADD $2 $8
ADD $59 $9
VALUE $2 _
SUB $65 $30
ADD 135 $65
ADD $71 $93
ADD $96 $67
ADD $6 $38
SUB $5 $8
SUB $67 $1
ADD $4 $71
VALUE $67 _
SUB $93 $54
SUB $51 $3
ADD 993 -871
ADD $6 $6
SUB $71 $65
ADD $25 $60
VALUE $59 _
ADD $6 $51
SUB $63 $97
VALUE $67 _
SUB 3 $59
ADD $88 $3
SUB $83 $53
SUB $50 $49
ADD $60 865
VALUE $53 _
SUB $29 $44
SUB $96 $25
ADD $21 $77
SUB $14 $30
SUB $27 $50
ADD $51 $5
SUB $40 $72
VALUE $90 _
ADD $87 $42
ADD $9 $47
SUB $97 $1
ADD $21 $44
ADD $78 $94
ADD $21 $71
ADD -730 $67
SUB $21 $89
SUB $83 $25
ADD $47 $84
ADD $6 $65
ADD $32 $22
ADD $27 $59
ADD $63 $11
ADD $65 $60
ADD $59 $6
SUB $1 $27
ADD $27 $83
SUB $19 $61")))))
