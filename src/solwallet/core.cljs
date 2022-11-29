(ns solwallet.core (:require [helix.core :refer [defnc $]]
                             [helix.hooks :as hooks]
                             ["react-dom/client" :as rdom]
                             ["@solana/wallet-adapter-react" :refer [ConnectionProvider WalletProvider]]
                             ["@solana/wallet-adapter-wallets" :refer [PhantomWalletAdapter]]
                             ["@solana/wallet-adapter-react-ui" :refer [WalletModalProvider]]
                             [solwallet.pages.home :refer [Home]]))


(defnc app [] 
  (let [endpoint "https://solana-mainnet.g.alchemy.com/v2/5PkwwRC9UrVxRD4qnINIZWaPVqPHFgEb"
        wallets (hooks/use-memo [] (clj->js [(new PhantomWalletAdapter)]))] 
    ($ ConnectionProvider {:endpoint endpoint} 
       ($ WalletProvider {:wallets wallets :autoConnect true}
          ($ WalletModalProvider
             ($ Home))))
    ))

(defn ^:export init []
  (let [root (rdom/createRoot (js/document.getElementById "app"))]
    (.render root ($ app))
   ))