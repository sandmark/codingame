(ns codeingame.easy.card-counting-easy-test
  (:require [clojure.test :refer [deftest testing is are]]
            [matcher-combinators.clj-test]
            [codingame.easy.card-counting-easy :as sut]
            [testdoc.core]))

(deftest card-counting-easy-test
  (testing "Helper"
    (is (match? sut/deck (sut/draw-card sut/deck \s)))
    (is (match? [\2 \2 \2 \3 \3 \3 \4 \4 \4]
                (sut/consciousness->cards "222.333.444.some")))
    (is (match? {\2 1 \3 1 \4 1}
                (sut/remove-cards sut/deck [\2 \2 \2 \3 \3 \3 \4 \4 \4])))
    (is (match? 2
                (sut/count-under-bust {\A 0, \J 0, \K 0, \Q 0, \2 1, \3 1, \4 1, \T 0, \5 0, \6 0, \7 0, \8 0, \9 0}
                                      4)))
    (is (match? "67%"
                (sut/solve {:bust 4 :consciousnesses-str "222.333.444.some distraction.555.5.678.678.678.678.another distraction.9999.TTTT.JJJJ.QQQQ.KKKK.AAAA"})))
    (is (match? []
                (sut/consciousness->cards "QUEEN.Oceans22"))))

  (testing "IO"
    (testing "Case 1"
      (let [input  "222.333.444.some distraction.555.5.678.678.678.678.another distraction.9999.TTTT.JJJJ.QQQQ.KKKK.AAAA\n4"
            output "67%\n"]
        (is (match? output
                    (with-in-str input
                      (with-out-str (-> (sut/input) sut/solve sut/output)))))))

    (testing "Case 2"
      (let [input  "Did I turn the iron off?.Did I turn the iron off?.AT3A.7JA.J.Oceans11.95A.mob boss.IRS.hungry.Cute dealer.2.45T84Q.Show Girls!!.QQQ.24868.QUEEN.K837695.Is that Penn or Teller?.362436.KJ7KJ\n7"
            output "25%\n"]
        (is (match? output
                    (with-in-str input
                      (with-out-str (-> (sut/input) sut/solve sut/output)))))))))
