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

(defn create-human-player
  "Creates a default human player"
  [& input]
  {:type "human" :game-piece nil :input (first input)})

(defn create-computer-player
  "Creates a computer player"
  [& input]
  {:type "computer" :game-piece nil :input (first input)})

(defn get-player-idx
  "Gets the index of a player in a game by type"
  [game player-type]
  (.indexOf (map #(:type %) (:players game)) player-type))

(defn set-player-piece
  "Sets a player's piece"
  [game player piece]
  (let [player-idx (get-player-idx game (:type player))]
    (-> (assoc-in game [:players player-idx :game-piece] piece)
        (assoc-in      [:turn :player :game-piece] piece))))

(defn update-game-move 
  "Updates the game once a move has been made"
  [game player move]
  (let [turn  (assoc-in (:turn game) [:position] move)
        board (assoc-in (:board game) move (:game-piece player))
        next-player-idx ({0 1 1 0} (get-player-idx game (:type player)))
        next-player ((:players game) next-player-idx)]
      (-> (assoc-in game [:board]        board)
          (assoc-in      [:last-turn]    turn)
          (assoc-in      [:turn :player] next-player))))

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

(defn is-game-over
  "Returns true or winning player if game is over. True indicates no one won.
   Returns false if the game is not yet over."
  [game]
  (let [winner (detect-win game)]
    (if winner
      winner
      (if (board-filled (:board game))
        true))))
