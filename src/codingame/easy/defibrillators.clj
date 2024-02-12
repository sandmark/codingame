(ns codingame.easy.defibrillators
  (:require [codingame.core :refer [output debug]]
            [clojure.string :as str]
            [clojure.math :as math]))

;; Goal: 現在地からもっとも近い除細動器の名称を出力。
;;
;; Input:
;; 3,879483  - ユーザーの経度
;; 43,608177 - ユーザーの緯度
;; 3         - モンペリエ市内に設置されている除細動器の数
;; 1;Maison de la Prevention Sante;6 rue Maguelone 340000 Montpellier;;3,87952263361082;43,6071285339217
;; 2;Hotel de Ville;1 place Georges Freche 34267 Montpellier;;3,89652239197876;43,5987299452849
;; 3;Zoo de Lunaret;50 avenue Agropolis 34090 Mtp;;3,87388031141133;43,6395872778854
;;
;; 除細動器の場所:
;;   除細動器識別番号;名称;住所;連絡先電話番号;経度;緯度
;;
;; Note:
;; 入力数値を扱う場合、カンマをドットに置き換えること
;;

(def earth-radius 6371)

(defn distance [lon-a lat-a {:keys [lon lat]}]
  (let [[lon-a lat-a lon-b lat-b] (map math/to-radians [lon-a lat-a lon lat])
        x                         (* (- lon-b lon-a) (math/cos (/ (+ lat-a lat-b) 2))) y (- lat-b lat-a)]
    (* earth-radius (math/sqrt (+ (math/pow x 2) (math/pow y 2))))))

(defn str->degree [s]
  (-> s (str/replace #"," ".") Double/parseDouble))

(defn ->defibrillator [s]
  (-> (zipmap [:id :name :address :phone :lon :lat] (str/split s #";"))
      (update :id #(Integer/parseInt %))
      (update :lon str->degree)
      (update :lat str->degree)))

(defn closest [lon lat defibs]
  (first (sort-by (partial distance lon lat) defibs)))

(defn defibrillators []
  (let [lon    (str->degree (read-line))
        lat    (str->degree (read-line))
        n      (Integer/parseInt (read-line))
        defibs (->> (repeatedly n #(.readLine *in*))
                    (map ->defibrillator))]
    (output (:name (closest lon lat defibs)))))
