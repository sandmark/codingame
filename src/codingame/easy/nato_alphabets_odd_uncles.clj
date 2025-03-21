(ns codingame.easy.nato-alphabets-odd-uncles
  (:require [clojure.string :as str]
            [clojure.set :as set]))

;; The NATO alphabet is the standard now for using a word to
;; indicate the letter that word starts with:
;; "Lima Echo Oscar" for "Leo" (for example)

;; But did you know that there have been
;; multiple other attempts at this?
;; (Just for fun, you can read more about that
;;  fascinating history/story at the MentalFloss link far below.)

;; Following are 4 of those alphabet-attempts
;; along with the year each started.

;; Your task is to:
;; ~ upgrade the input to the next version, or
;; ~ if it's already in the newest version:
;;   degrade it to the oldest version

;; ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
;; Year-1908: Authority, Bills, Capture, Destroy, Englishmen, Fractious, Galloping, High, Invariably, Juggling, Knights, Loose, Managing, Never, Owners, Play, Queen, Remarks, Support, The, Unless, Vindictive, When, Xpeditiously, Your, Zigzag
;; Year-1917: Apples, Butter, Charlie, Duff, Edward, Freddy, George, Harry, Ink, Johnnie, King, London, Monkey, Nuts, Orange, Pudding, Queenie, Robert, Sugar, Tommy, Uncle, Vinegar, Willie, Xerxes, Yellow, Zebra
;; Year-1927: Amsterdam, Baltimore, Casablanca, Denmark, Edison, Florida, Gallipoli, Havana, Italia, Jerusalem, Kilogramme, Liverpool, Madagascar, New-York, Oslo, Paris, Quebec, Roma, Santiago, Tripoli, Uppsala, Valencia, Washington, Xanthippe, Yokohama, Zurich
;; Year-1956: Alfa, Bravo, Charlie, Delta, Echo, Foxtrot, Golf, Hotel, India, Juliett, Kilo, Lima, Mike, November, Oscar, Papa, Quebec, Romeo, Sierra, Tango, Uniform, Victor, Whiskey, X-ray, Yankee, Zulu
;; ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

;; Sources:
;; https://www.mentalfloss.com/article/504886/4-phonetic-alphabets-didnt-survive
;; https://owenspencer-thomas.com/journalism/media-tips/spelling-out-words/

;;; -----
;;; Input
;;; -----
;; Line 1: A word spelled out using an alphabet

;;; ------
;;; Output
;;; ------
;; Line 1: That same word spelled out with a different alphabet

;;; -----------
;;; Constraints
;;; -----------

;;; -------
;;; Example
;;; -------
;; Input: Authority Bills Capture
;; Output: Apples Butter Charlie

(def nato-alphabets
  [["Authority" "Bills" "Capture" "Destroy" "Englishmen" "Fractious" "Galloping" "High" "Invariably" "Juggling" "Knights" "Loose" "Managing" "Never" "Owners" "Play" "Queen" "Remarks" "Support" "The" "Unless" "Vindictive" "When" "Xpeditiously" "Your" "Zigzag"]
   ["Apples" "Butter" "Charlie" "Duff" "Edward" "Freddy" "George" "Harry" "Ink" "Johnnie" "King" "London" "Monkey" "Nuts" "Orange" "Pudding" "Queenie" "Robert" "Sugar" "Tommy" "Uncle" "Vinegar" "Willie" "Xerxes" "Yellow" "Zebra"]
   ["Amsterdam" "Baltimore" "Casablanca" "Denmark" "Edison" "Florida" "Gallipoli" "Havana" "Italia" "Jerusalem" "Kilogramme" "Liverpool" "Madagascar" "New-York" "Oslo" "Paris" "Quebec" "Roma" "Santiago" "Tripoli" "Uppsala" "Valencia" "Washington" "Xanthippe" "Yokohama" "Zurich"]
   ["Alfa" "Bravo" "Charlie" "Delta" "Echo" "Foxtrot" "Golf" "Hotel" "India" "Juliett" "Kilo" "Lima" "Mike" "November" "Oscar" "Papa" "Quebec" "Romeo" "Sierra" "Tango" "Uniform" "Victor" "Whiskey" "X-ray" "Yankee" "Zulu"]])

(defn words->year [words]
  (let [word-set (set words)
        f        (fn [i x] (when (set/subset? word-set (set x)) i))]
    (first (keep-indexed f nato-alphabets))))

(defn word->idx [word]
  (-> (str/upper-case word) first int (- 65)))

(defn solve [l]
  (let [words     (str/split l #" ")
        y         (rem (inc (words->year words)) (count nato-alphabets))
        next-word #(get-in nato-alphabets [y (word->idx %)])
        ids       (mapv next-word words)]
    (str/join " " ids)))

(defn read-input []
  (read-line))

(defn output [s]
  (println s))

(comment
  (-> (read-input) solve output))
