(ns solwallet.pages.home (:require [helix.core :refer [defnc $]]
                                   [helix.hooks :as hooks]
                                   [helix.dom :as d]
                                   ["@solana/web3.js" :refer [PublicKey]]
                                   ["@solana/wallet-adapter-base" :refer [WalletNotConnectedError]]
                                   ["@solana/wallet-adapter-react" :refer [useConnection useWallet]]
                                   [promesa.core :as p]
                                   ["@solana/spl-token" :refer [TOKEN_PROGRAM_ID AccountLayout]]
                                   ["@solana/wallet-adapter-react-ui" :refer [WalletModalProvider WalletDisconnectButton WalletMultiButton]]))


(defnc Home []
  (let [[balance set-balance] (hooks/use-state "0")
        wallet (useWallet)
        public-key (. wallet -publicKey)
        connection (. (useConnection) -connection)
        usdt-token-account (new PublicKey "C2Urfcmb1qWEEX8tx6EPu2UbVNtpvJL1oND8AVEMpcMA")
        get-balance (fn [] 
                         (p/let [response (. connection (getBalance public-key))
                                 bal (/ response 1000000000)]
                           (set-balance bal)))]
    
    (hooks/use-effect [wallet] (if (= nil public-key) (set-balance "0")(get-balance)))

    (d/main
     (d/h3 "Welcome" (d/span {:className "user"} (str (if public-key (str " " public-key)) ",")))
     (d/div {:className "container"} 
              (d/div {:className "container_left"}
                     (if public-key 
                       (d/p "Balance: " balance " SOL")
                       (d/p "Please connect your wallet")))
            (d/div {:className "container_right"}
                   ($ WalletMultiButton))
            ))
    ))













(comment

  ;; GETS ALL SPL TOKEN ACCOUNT BALANCE AND TOKEN ADDRESS

  (p/let [response (. connection (getTokenAccountsByOwner public-key (clj->js {:programId TOKEN_PROGRAM_ID})))
          data (. response -value)]
    (set-balance (/ response 1000000000))
    (doseq [i data]
      (let [account-data (. AccountLayout (decode (.. i -account -data)))]
        (js/console.log (. account-data -amount))
        (js/console.log (.. account-data -mint toString)))))
  )