(ns solwallet.components.other (:require [helix.core :refer [defnc]]
                                         [helix.dom :as d]
                                        ;;  @solana/spl-token" :refer [TOKEN_PROGRAM_ID AccountLayout]]
                                         ))


(defnc OtherComponent []
  (d/div {:className "container2_right"}
         (d/p "Under construction!")))






;; (comment

;;   ;; GETS ALL SPL TOKEN ACCOUNT BALANCE AND TOKEN ADDRESS

;;   (p/let [response (. connection (getTokenAccountsByOwner public-key (clj->js {:programId TOKEN_PROGRAM_ID})))
;;           data (. response -value)]
;;     (set-balance (/ response 1000000000))
;;     (doseq [i data]
;;       (let [account-data (. AccountLayout (decode (.. i -account -data)))]
;;         (js/console.log (. account-data -amount))
;;         (js/console.log (.. account-data -mint toString)))))
;;   )