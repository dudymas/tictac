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
  evaluates to anything other than true. 
  Prints out strings from filter to CLI."
  [input-filter & start-text]
  (if (string? start-text) (println start-text))
  ((fn []
    (let [input (read-line)
          filter-result (input-filter input)]
      (if (true? filter-result)
        input
        (do
          (if (string? filter-result)
            (println filter-result))
          (recur)))))))

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

(defn display-board 
  "Prints out board"
  [board]
  (let [pos (atom 0)
        translate #(do (if (nil? %) @pos ({:X "X" :O "O"} %)))
        incr-board-position #(do (swap! pos inc) (translate %))]
    (map #(map incr-board-position %) board)))

(defn ask-player-for-piece
  "Depending on player, gets their game-piece of choice."
  [game player]
  (if (= "human" (:type player))
    (do
      (println "Please select a game piece ('X' or 'O')")
      (let [get-input (:input player)
            filter-choices  #(and (string? %) (.contains #{"X" "O"} (.toUpperCase %)))
            choice (get-input filter-choices)
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

(defn print-board
  "Prints the board to the CLI"
  [board]
  (println (-> (apply str (map #(apply str %) (display-board board)))
                (clojure.string/replace #"\w{3}" "$0\n")
                (clojure.string/replace #"\w" " $0 "))))

(defn init-player 
  "Checks that a player is ready to play, and if not, asks some questions"
  [game player]
  ;;for now, we just have one question
  ;;later we might let the player choose who goes first and the like
  (if (nil? (:game-piece player))
      (ask-player-for-piece game player)
      game))

(defn take-turn
  "Given a game with a next turn, uses the current player to get choices"
  [game]
  (let [ready-game (init-player game (:player (:turn game)))
        player (:player (:turn ready-game))]
    (if (= "human" (:type player))
      (let [move (ask-player-for-move player)]
        (update-game-move ready-game player move))
      (let [move  (get-computer-move ready-game)
            updated-game (update-game-move ready-game player move)]
        (print-board (:board updated-game))
        updated-game))))

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
