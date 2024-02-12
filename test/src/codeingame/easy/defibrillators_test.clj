(ns codeingame.easy.defibrillators-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.defibrillators :as sut]))

(deftest defibrillators-test
  (testing "Data Structure"
    (is (match? 3.87952263361082
                (sut/str->degree "3,87952263361082")))

    (testing "Record"
      (is (match? {:id      1
                   :name    "Maison de la Prevention Sante"
                   :address "6 rue Maguelone 340000 Montpellier"
                   :phone   empty?
                   :lon     3.87952263361082
                   :lat     43.6071285339217}
                  (sut/->defibrillator
                   "1;Maison de la Prevention Sante;6 rue Maguelone 340000 Montpellier;;3,87952263361082;43,6071285339217")))))
  (testing "Math"
    (testing "Closest"
      (is (match? {:id 1}
                  (sut/closest 3.879483 43.608177
                               (map sut/->defibrillator
                                    ["1;Maison de la Prevention Sante;6 rue Maguelone 340000 Montpellier;;3,87952263361082;43,6071285339217"
                                     "2;Hotel de Ville;1 place Georges Freche 34267 Montpellier;;3,89652239197876;43,5987299452849"
                                     "3;Zoo de Lunaret;50 avenue Agropolis 34090 Mtp;;3,87388031141133;43,6395872778854"]))))))

  (testing "Problem"
    (is (match? "Maison de la Prevention Sante\n"
                (with-out-str
                  (with-in-str
                    "3,879483
43,608177
3
1;Maison de la Prevention Sante;6 rue Maguelone 340000 Montpellier;;3,87952263361082;43,6071285339217
2;Hotel de Ville;1 place Georges Freche 34267 Montpellier;;3,89652239197876;43,5987299452849
3;Zoo de Lunaret;50 avenue Agropolis 34090 Mtp;;3,87388031141133;43,6395872778854"
                    (sut/defibrillators)))))))
