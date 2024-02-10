(ns codeingame.easy.ascii-art-test
  (:require [codingame.easy.ascii-art :as sut]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.clj-test]))

(def aa-4x5 " #  ##   ## ##  ### ###  ## # # ###  ## # # #   # # ###  #  ##   #  ##   ## ### # # # # # # # # # # ### ###
# # # # #   # # #   #   #   # #  #    # # # #   ### # # # # # # # # # # #    #  # # # # # # # # # #   #   #
### ##  #   # # ##  ##  # # ###  #    # ##  #   ### # # # # ##  # # ##   #   #  # # # # ###  #   #   #   ##
# # # # #   # # #   #   # # # #  #  # # # # #   # # # # # # #    ## # #   #  #  # # # # ### # #  #  #
# # ##   ## ##  ### #    ## # # ###  #  # # ### # # # #  #  #     # # # ##   #  ###  #  # # # #  #  ###  #  ")

(deftest ascii-art-test
  (testing "Fontify"
    (is (match? {\A [" #  "
                     "# # "
                     "### "
                     "# # "
                     "# # "]}
                (sut/fontify 4 5 aa-4x5))))

  (testing "Character"
    (testing "Always upper case"
      (is (match? [\H \E \L \L \O]
                  (sut/str->keys "hello"))))
    (testing "Invalid letter should be replaced with `?`"
      (is (match? [\H \I \?]
                  (sut/str->keys "hi!")))))

  (testing "Render"
    (let [font (sut/fontify 4 5 aa-4x5)]
      (is (match? "### \n#   \n##  \n#   \n### "
                  (sut/render font "e")))))

  (testing "WithInput"
    (is (match? "### \n#   \n##  \n#   \n### \n"
                (with-out-str
                  (with-in-str "4
5
E
 #  ##   ## ##  ### ###  ## # # ###  ## # # #   # # ###  #  ##   #  ##   ## ### # # # # # # # # # # ### ###
# # # # #   # # #   #   #   # #  #    # # # #   ### # # # # # # # # # # #    #  # # # # # # # # # #   #   #
### ##  #   # # ##  ##  # # ###  #    # ##  #   ### # # # # ##  # # ##   #   #  # # # # ###  #   #   #   ##
# # # # #   # # #   #   # # # #  #  # # # # #   # # # # # # #    ## # #   #  #  # # # # ### # #  #  #
# # ##   ## ##  ### #    ## # # ###  #  # # ### # # # #  #  #     # # # ##   #  ###  #  # # # #  #  ###  #  "
                    (sut/ascii-art))))))  )
