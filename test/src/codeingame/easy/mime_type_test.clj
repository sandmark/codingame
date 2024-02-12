(ns codeingame.easy.mime-type-test
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]
            [codingame.easy.mime-type :as sut]))

(deftest mime-type-test
  (testing "Mime Type"
    (testing "Dictionary"
      (is (match? {"html" "text/html"
                   "png"  "image/png"
                   "gif"  "image/gif"}
                  (sut/make-dict ["HTML text/html" "png image/png" "gif image/gif"])))
      (is (match? "text/html"
                  (sut/get-mime-type {"html" "text/html"} "index.HtMl")))
      (is (match? "UNKNOWN"
                  (sut/get-mime-type {} "index.html")))
      (is (match? "UNKNOWN"
                  (sut/get-mime-type {"index" "mismatch"} "index"))))

    (testing "Extension"
      (is (match? "html"
                  (sut/file-ext "index.html")))
      (is (match? nil
                  (sut/file-ext "index")))
      (is (match? "tmp"
                  (sut/file-ext "b.wav.tmp")))
      (is (match? "pdf"
                  (sut/file-ext "report..pdf")))
      (is (match? empty?
                  (sut/file-ext ".mp3."))))

    (testing "Problem"
      (testing "Example"
        (let [input "3\n3\nhtml text/html\npng image/png\ngif image/gif\nanimated.gif\nportrait.png\nindex.html"]
          (is (match? "image/gif\nimage/png\ntext/html\n"
                      (with-out-str
                        (with-in-str input
                          (sut/mime-type))))))))))
