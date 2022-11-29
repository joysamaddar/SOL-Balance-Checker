(ns solwallet.pages.home (:require [helix.core :refer [defnc $]]
                                   [helix.hooks :as hooks]
                                   [helix.dom :as d]
                                   ["@solana/wallet-adapter-react" :refer [useConnection useWallet]]
                                   [promesa.core :as p]
                                   ["@solana/wallet-adapter-react-ui" :refer [WalletMultiButton]]
                                   [solwallet.components.transfer :refer [TransferComponent]]
                                   [solwallet.components.other :refer [OtherComponent]]))


(defnc Home []
  (let [[balance set-balance] (hooks/use-state 0)
        [tab set-tab] (hooks/use-state "transfer")
        wallet (useWallet)
        public-key (. wallet -publicKey)
        connection (. (useConnection) -connection)
        get-balance (fn []
                      (p/let [response (. connection (getBalance public-key))
                              bal (/ response 1000000000)]
                        (set-balance bal)))]

    (hooks/use-effect [wallet] (if (= nil public-key) (set-balance 0) (get-balance)))

    (d/main
     (d/h3 "Welcome" (d/span {:className "user"} (str (if public-key (str " " public-key) "") ",")))
     (d/div {:className "container"}
            (d/div {:className "container_left"}
                   (if public-key
                     (d/p "Balance: " balance " SOL")
                     (d/p "Please connect your wallet")))
            (d/div {:className "container_right"}
                   ($ WalletMultiButton)))

     (if public-key (d/div {:className "container2"}
                           (d/div {:className "container2_left"}
                                  (d/p {:className (if (= tab "transfer") (str "active") (str "")) :on-click #(set-tab "transfer")} "Transfer SOL")
                                  (d/p {:className (if (= tab "other") (str "active") (str "")) :on-click #(set-tab "other")} "Other"))
                           (if (= tab "transfer")
                             ($ TransferComponent {:wallet wallet :connection connection :public-key public-key :get-balance get-balance :balance balance}) ())
                           (if (= tab "other")
                             ($ OtherComponent) ())) ()))))
