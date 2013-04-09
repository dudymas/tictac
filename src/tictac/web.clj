;;borrowing from http://github.com/magomimmo/modern-cljs
(ns tictac.web
  (:use tictac.bestmove
        tictac.core)
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [resources not-found]]
            [compojure.handler :refer [site]]
            [clojure.data.json :as json]
            [ring.util.response :as resp]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/write-str data)})

(defn json-value-parser
  ([key]
    (json-value-parser nil key))
  ([key value]
    (if (.contains ["X" "O"] value)
      (keyword value)
      value)))

(defn parse-json-game
  "Parses a game from json."
  [bod]
  (with-open [rdr (clojure.java.io/reader bod)] ;check out mah bod
    (let [parse-board
            (comp vec (partial map (comp vec (partial map keyword))));(("a" "b")("c" "d")) -> [[:a :b][:c :d]]
          parse-game
            #(assoc-in % [:board] (parse-board (:board %))) ;parse the board, and return game with new board
          game
            (json/read-str (slurp rdr) :key-fn keyword :value-fn json-value-parser)]
      (parse-game game))))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
  (GET ""  [] (resp/resource-response "index.html" {:root "public"}))
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  ;; ask computer what they'd move next
  (POST "/move" {bod :body}
    (json-response
      (get-computer-move
        (parse-json-game bod))))
  (POST "/detect-win" {bod :body}
    (json-response
      (detect-win
        (parse-json-game bod))))
  ;; to server static pages saved in resources/public directory
  (resources "/")
  ;; if page is not found
  (not-found "Page not found"))

;;; site function create an handler suitable for a standard website,
;;; adding a bunch of standard ring middleware to app-route:
(def handler
  (site app-routes))