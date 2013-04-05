(ns tictac.remotes
  (:use tictac.bestmove)
  (:require [compojure.handler         :refer [site]]
            [tictac.web                :refer [handler]]
            [shoreleave.middleware.rpc :refer [defremote wrap-rpc]]))

(defremote request-computer-move
  [game]
  (get-computer-move game))

(def web (-> (var handler) (wrap-rpc) (site)))