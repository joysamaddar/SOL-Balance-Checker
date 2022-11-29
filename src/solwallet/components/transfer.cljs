(ns solwallet.components.transfer (:require [helix.core :refer [defnc]]
                                            [helix.hooks :as hooks]
                                            [helix.dom :as d]
                                            ["@solana/web3.js" :refer [SystemProgram Transaction LAMPORTS_PER_SOL]]
                                            ["@solana/wallet-adapter-base" :refer [WalletNotConnectedError]]
                                            [promesa.core :as p]))



(defnc TransferComponent [{:keys [wallet connection public-key get-balance balance]}]

  (let [[address, set-address] (hooks/use-state "")
        [amount, set-amount] (hooks/use-state 0)
        [result, set-result] (hooks/use-state "")
        transferHandler (hooks/use-callback :auto-deps (fn [e]
                                                         (. e preventDefault)
                                                         (if (= nil public-key) (throw (new WalletNotConnectedError)) ())
                                                         (p/let [transaction (. (new Transaction) (add (. SystemProgram (transfer (clj->js {:fromPubkey public-key :toPubkey address :lamports (* LAMPORTS_PER_SOL amount)})))))]
                                                           (p/let [data (. connection getLatestBlockhashAndContext)
                                                                   slot (.. data -context -slot)]
                                                             (p/let [_ (. wallet (sendTransaction transaction connection {:minContextSlot slot}))]
                                                               (set-result (str "Sent " amount " SOL to " address "!"))
                                                               (js/setTimeout #((set-result "")(get-balance)) 4000)
                                                               (set-address "")
                                                               (set-amount 0))))))]


    (d/div {:className "container2_right"}
           (d/form {:on-submit transferHandler}
                   (d/label {:for "address"} "Enter receivers wallet address")
                   (d/input {:id "address" :value address :on-change #(set-address (.. % -target -value)) :required true})
                   (d/label {:for "address"} "Enter amount to send" (d/span {:className "max" :on-click #(set-amount (- balance 0.001))} " (Max) "))
                   (d/input {:id "amount" :type "number" :value amount :on-change #(set-amount (.. % -target -value)) :required true})
                   (d/button  "Transfer"))
           (if (not= result "") (d/p {:className "notification"} result) ()))))