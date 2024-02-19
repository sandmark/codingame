(ns codingame.easy.decode-the-message
  "As an FBI agent you intercepted a message from terrorists.
  This message has to be decrypted, and to do so you have access to 2 pieces of information : P and C.

  C is the alphabet used to write the message, and contains all the letters/characters needed to decode it.

  P corresponds to the encoded value of the message.
  Thankfully, you happen to know how this value is computed, and now need to reverse the following process :
  - Assuming the alphabet is 'abcd', each letter is associated to its index : a = 0, b = 1, c = 2, d = 3.
  - For the following values, a new letter is either added or switched :
  - aa = 4, ba = 5, ca = 6, da = 7
  - ab = 8, bb = 9, cb = 10, db = 11
  - ...
  - aaa = 20, baa = 21, caa = 22, daa = 23, and so on.
  - The whole message gets a unique value this way. For example with a full alphabet (26 letters) 'hello' would be 7073801.

  Get it done agent ! (Good Luck)"
  {:doc-ja
   "FBI捜査官のあなたは、ある日テロリストからのメッセージを傍受した。
  メッセージの解読には2つの情報、PとCが必要だ。

  Cはメッセージを書くのに使われるアルファベットで、解読に必要なすべての文字が含まれている。

  Pはメッセージのエンコードに使われる値に対応している。
  天の助けか、この値がどのように計算されるかはわかっている。あとは次の手順を反転させるだけだ。
  - アルファベットの並びを'abcd'だと仮定すると、これらはインデックスと結び付けられる: a = 0, b = 1, c = 2, d = 3
  - 新しい文字が追加、入れ替えされた場合、以下の値となる。
  - aa = 4, ba = 5, ca = 6, da = 7
  - ab = 8, bb = 9, cb = 10, db = 11
  - ...
  - aaa = 20, baa = 21, caa = 22, daa = 23 と続く。
  - これにより、メッセージ全体が一意の値となる。例えば全アルファベット(26文字)を使用した'hello'は7073801となるだろう。

  必ずや解決するのだ! (がんばってね)"})

(defn solve
  "Returns a string which means `n` in (count `letters`) based number.

  (solve 4 \"abcd\")
  ;; => \"aa\"

  (solve 23 \"abcd\")
  ;; => \"daa\"
  "
  [n letters]
  (let [base (count letters)]
    (loop [x n, result []]
      (if (< x base)
        (->> (conj result x) (map (partial nth letters)) (apply str))
        (recur (dec (quot x base)) (conj result (rem x base)))))))

(defn input []
  (let [[n' letters] (-> *in* java.io.BufferedReader. line-seq)]
    [(Long. n') letters]))

(defn output [s]
  (println s)
  (flush))
