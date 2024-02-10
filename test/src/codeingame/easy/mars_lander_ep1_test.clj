(ns codeingame.easy.mars-lander-ep1-test
  (:require [codingame.easy.mars-lander-ep1 :as sut]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]))

(deftest mars-lander-ep1-test
  (testing "Input value"
    (testing "Initial"
      (with-in-str "6\n0 1500\n1000 2000\n2000 500\n3500 500\n5000 1500\n6999 1000\n2500 2500 0 -3 500 9 1\n"
        (is (match? [[0 1500] [1000 2000] [2000 500] [3500 500] [5000 1500] [6999 1000]]
                    (sut/get-init-input *in*)))))

    (testing "Turn"
      (with-in-str "2500 2500 0 -3 500 0 1\n"
        (is (match? {:x 2500 :y 2500 :h 0 :v -3 :fuel 500 :rotate 0 :power 1}
                    (sut/get-turn-input *in*)))))

    (testing "Body"
      #_(with-in-str "6\n0 1500\n1000 2000\n2000 500\n3500 500\n5000 1500\n6999 1000\n2500 2500 0 -3 500 0 1\n"
          (mlep1))))

  (testing "Speed"
    (is (match? -3.711
                (sut/next-vspeed 0 0)))
    (is (match? -6.422
                (sut/next-vspeed (- sut/mg) 1)))))
