(ns solwallet.core (:require [helix.core :refer [defnc $]]
                             [helix.hooks :as hooks]
                             [helix.dom :as d]
                             ["react-dom/client" :as rdom]
                             ["@solana/wallet-adapter-react" :refer [ConnectionProvider WalletProvider]]
                             ["@solana/wallet-adapter-wallets" :refer [UnsafeBurnerWalletAdapter PhantomWalletAdapter]]
                             ["@solana/wallet-adapter-react-ui" :refer [WalletModalProvider WalletDisconnectButton]]
                             [solwallet.pages.home :refer [Home]]))


(defnc app [] 
  (let [endpoint "https://solana-mainnet.g.alchemy.com/v2/5PkwwRC9UrVxRD4qnINIZWaPVqPHFgEb"
        wallets (hooks/use-memo [] (clj->js [(new PhantomWalletAdapter) (new UnsafeBurnerWalletAdapter)]))] 
    ($ ConnectionProvider {:endpoint endpoint} 
       ($ WalletProvider {:wallets wallets :autoConnect true}
          ($ WalletModalProvider
             ($ Home))))
    ))

(defn init []
  (let [root (rdom/createRoot (js/document.getElementById "app"))]
    (.render root ($ app))
   ))