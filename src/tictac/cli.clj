(ns tictac.cli
  (:use tictac.core
        tictac.bestmove))

(def play)

(defn -main
  "Runs a command-line interface for a game of tic tac toe."
  [& args]
  (println "hello world")
  (play))

(defn get-input-while
  "Continues to get input while a input filter
  evaluates to anything other than true. Prints out filter to CLI."
  [input-filter & start-text]
  (if (string? start-text) (println start-text))
  ((fn []
    (let [filter-result ()]
      (if (string? filter-result) (println filter-result))))))

(defn handle-end-game
  "Examines game results, displays conclusion to the player, then
  waits for any key before ending the game."
  [game game-result]
  (if (true? game-result)
    (println "Stalemate!")
    (if (:type game-result)
      (if (= "human" (:type game-result))
        (println "You won!")
        (if (= "computer" (:type game-result))
          (println "Computer won!")))))
  (println "Press any key to quit.")
  (read-line))

(defn ask-player-for-piece
  "Depending on player, gets their game-piece of choice."
  [game player]
  (if (= "human" (:type player))
    (do
      (println "Please select a game piece ('X' or 'O')")
      (let [get-input (:input player)
            choice (get-input #(and (string? %)
                                    (.contains #{"X" "O"} (.toUpperCase %))))
            piece ({"O" :O "X" :X} (.toUpperCase choice))]
        (set-player-piece game player piece)))
    (let [human-game-piece  (:game-piece ((:players game) (get-player-idx game "human")))
          computer-piece ({:X :O :O :X} human-game-piece)]
      (set-player-piece game player computer-piece))))

(defn ask-player-for-move 
  "Requests a move from the human player and updates the game session"
  [player]
  (let [parse-int #(read-string (clojure.string/replace % #"\D" ""))
        move ((:input player) 
          #(.contains (set (range 1 10)) (parse-int %)))
        parsed-move (parse-int move)]
    ;;given a move, we have two decodes. One for row, and another for column
    [({1 0   2 0   3 0
       4 1   5 1   6 1
       7 2   8 2   9 2} parsed-move)   ;;pick a row
    ({ 1 0   2 1   3 2
       4 0   5 1   6 2
       7 0   8 1   9 2} parsed-move)]));;pick a column

(defn take-turn
  "Given a game with a next turn, uses the current player to get choices"
  [game]
  (let [player (:player (:turn game))
        next-player-idx ({0 1 1 0} (get-player-idx game (:type player)))
        next-player ((:players game) next-player-idx)]
    (if (nil? (:game-piece player))
      (ask-player-for-piece)
      (if (= "human" (:type player))
        (let [move (ask-player-for-move player)
              turn (assoc-in (:turn game) [:position] move)
              board (assoc-in (:board game) move (:game-piece player))]
          (-> (assoc-in game [:board]        board)
              (assoc-in      [:last-turn]    turn)
              (assoc-in      [:turn :player] next-player)))
        (let [move (get-computer-move game)
              turn (assoc-in (:turn game) [:position] move)
              board (assoc-in (:board game) move (:game-piece player))]
          (-> (assoc-in game [:board]        board)
              (assoc-in      [:last-turn]    turn)
              (assoc-in      [:turn :player] next-player)))))))

(defn play
  "Plays a game of tic tac toe using the CLI"
  []
  (let [player-human (create-human-player get-input-while)
        player-computer (create-computer-player get-computer-move)
        game (start-game [player-human player-computer] (create-board))]
    (loop [game-in-progress (take-turn game)]
      (let [game-result (is-game-over game-in-progress)]
        (if (not game-result)
          (recur (take-turn game))
          (handle-end-game game game-result))))))
