(ns codingame.easy.turn-here-sign
  (:require [clojure.string :as str]))

;;; Line 1: string allInput, that is composed of
;;; - direction
;;; - howManyArrows
;;; - heightOfArrows
;;; - strokeThicknessOfArrows
;;; - spacingBetweenArrows
;;; - additionalIndentOfEachLine
;;;
;;; Example Input:
;;; right 3 9 8 6 2
;;;
;;; Example Output:
;; >>>>>>>>      >>>>>>>>      >>>>>>>>
;;   >>>>>>>>      >>>>>>>>      >>>>>>>>
;;     >>>>>>>>      >>>>>>>>      >>>>>>>>
;;       >>>>>>>>      >>>>>>>>      >>>>>>>>
;;         >>>>>>>>      >>>>>>>>      >>>>>>>>
;;       >>>>>>>>      >>>>>>>>      >>>>>>>>
;;     >>>>>>>>      >>>>>>>>      >>>>>>>>
;;   >>>>>>>>      >>>>>>>>      >>>>>>>>
;; >>>>>>>>      >>>>>>>>      >>>>>>>>

(defn indent-levels
  "Calculates the indentation levels for a graphical representation based on the given direction, height, and indent.

  Parameters:
  - direction: A keyword representing the direction of the graphical representation. It can be either `\\>` or `\\<`.
  - height: An integer representing the height of the graphical representation.
  - indent: An integer representing the indentation level for each line.

  Returns:
  A vector of integers representing the indentation levels for each line of the graphical representation.
  The vector has the same length as the height, and the values are calculated based on the direction and indent.

  Examples:
  (indent-levels {:direction \\> :height 9 :indent 2})
  ;; => [0 2 4 6 8 6 4 2 0]

  (indent-levels {:direction \\< :height 9 :indent 3})
  ;; => [12 9 6 3 0 3 6 9 12]
  "
  [{:keys [direction height indent]}]
  (let [f {\> identity \< reverse}
        v ((f direction) (range 0 (Math/ceil (/ height 2))))]
    (map (partial * indent) (concat v (reverse (butlast v))))))

(defn indent-line
  "Creates a formatted string representing a graphical line with indentation.

  Parameters:
  - line: A string representing the base line to be indented.
  - m: A map containing the following keys:
    :direction: A keyword representing the direction of the graphical representation. It can be either `\\>` or `\\<`.
    :height: An integer representing the height of the graphical representation.
    :indent: An integer representing the indentation level for each line.

  Returns:
  A string representing the indented graphical line. Each line is separated by a newline character.

  Example:
  (indent-line \">>>>>>>>\" {:direction \\> :height 3 :indent 2})
  ;; => \">>>>>>>>\\n  >>>>>>>>\\n>>>>>>>>\"

  (indent-line \"<<<\" {:direction \\< :height 3 :indent 3})
  ;; => \"   <<<\\n<<<\\n   <<<\"
  "
  [line {:keys [height] :as m}]
  (let [lvls  (indent-levels m)
        lines (repeat height line)
        f     #(str (apply str (repeat %1 \space)) %2)]
    (->> (map f lvls lines) (str/join "\n"))))

(defn create-part
  "Creates a string of repeated characters based on the given direction and thickness.

  (create-part {:direction \\> :thickness 8})
  ;; => \">>>>>>>>\"
  "
  [{:keys [direction thickness]}]
  (apply str (repeat thickness direction)))

(defn create-line
  "Creates a formatted string of repeated parts, separated by spaces.

  This function takes a string `part` and a map `options` containing the keys:
  - `:howmany`: an integer representing the number of times the `part` should be repeated.
  - `:spacing`: an integer representing the number of spaces to insert between each `part`.

  The function returns a string that consists of the `part` repeated `howmany` times,
  separated by `spacing` number of spaces.

  Example:
  (create-line \">>>>>>>>\" {:howmany 3 :spacing 6})
  ;; => \">>>>>>>>      >>>>>>>>      >>>>>>>>\"
  "
  [part {:keys [howmany spacing]}]
  (let [separater (apply str (repeat spacing \space))]
    (->> (repeat howmany part)
         (str/join separater))))

(defn render [m]
  (let [part (create-part m)
        line (create-line part m)]
    (indent-line line m)))

(defn read-input []
  (let [l                                             (first (doall (-> *in* java.io.BufferedReader. line-seq)))
        [dir howmany height thickness spacing indent] (str/split l #" ")
        direction                                     ({"right" \> "left" \<} dir)]
    {:direction direction
     :howmany   (Integer/parseInt howmany)
     :height    (Integer/parseInt height)
     :thickness (Integer/parseInt thickness)
     :spacing   (Integer/parseInt spacing)
     :indent    (Integer/parseInt indent)}))

(defn solve [m]
  (render m))

(defn output [s]
  (println s))

(comment
  (-> (read-input) solve output))
