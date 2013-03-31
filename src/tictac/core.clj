(ns tictac.core
  (:use tictac.board))

(keyword :X)
(keyword :O)

(defn create-board
  "Create a game board, which is a vector of rows. Each row is a horizontal row on the board."
  []
  [[nil nil nil] ;;row 1
   [nil nil nil] ;;row 2
   [nil nil nil]]);row 3

(defn start-game
  "Creates a game with two players."
  [player-list game-board]
  { :turn {:player (player-list 0)}
    :players player-list
    :board game-board
    :last-turn nil})

(defn is-row-complete
  "Returns true if the row given only contains the given piece"
  [row piece]
  (reduce
    (fn [a b] (and a (= b piece)))
    true row))

(defn detect-win
  "Returns a winner if someone has won with the :last-turn, otherwise nil"
  [game]
  (let [game-board (:board game)
        last-turn (:last-turn game)
        position (:position last-turn)
        piece (:game-piece (:player last-turn))
        row-list (get-adjacent-rows game-board position)
        adj-list (map #(get-adjacent-pieces game-board %) row-list)]
    (if (reduce
          (fn [result,next-row] (or result (is-row-complete next-row piece)))
          false
          adj-list)
      (:player last-turn))))
