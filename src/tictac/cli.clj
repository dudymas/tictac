(ns tictac.cli
  (:use tictac.core
        tictac.bestmove
        tictac.board))

(def play)

(defn -main
  "Runs a command-line interface for a game of tic tac toe."
  [& args]
  (play))

(defn get-input-while
  "Continues to get input while a input filter
  evaluates to anything other than true. 
  Prints out strings from filter to CLI."
  [input-filter & start-text]
  (if (string? (first start-text)) (println (first start-text)))
  ((fn []
    (let [input (read-line)
          filter-result (input-filter input)]
      (if (true? filter-result)
        input
        (do
          (if (string? filter-result)
            (println filter-result))
          (recur)))))))

(defn display-board 
  "Prints out board"
  [board]
  (let [move (atom 0)
        translate #(do (if (nil? %) @move ({:X "X" :O "O"} %)))
        incr-board-position #(do (swap! move inc) (translate %))]
        ;;there probably is an easier way to do with with map-indexed
    (map #(map incr-board-position %) board)))

(defn print-board
  "Prints the board to the CLI"
  [board]
  (println (-> (apply str (map #(apply str %) (display-board board)))
                (clojure.string/replace #"\w{3}" "$0\n")
                (clojure.string/replace #"\w" " $0 "))))

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
  [board player]
  (print-board board)
  (let [parse-int #(read-string (clojure.string/replace % #"\D" ""))
        get-input (:input player)
        move (get-input 
          #(.contains (set (range 1 10)) (parse-int %))
          "Please make a move by entering an available number...")
        parsed-move (parse-int move)]
    ;;given a move, we have two decodes. One for row, and another for column
    (get-position-by-idx parsed-move)));;pick a column

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
      (let [move (ask-player-for-move (:board game) player)]
        (update-game-move ready-game player move))
      (let [move  (get-computer-move ready-game)
            updated-game (update-game-move ready-game player move)]
        updated-game))))

(defn handle-end-game
  "Examines game results, displays conclusion to the player, then
  waits for any key before ending the game."
  [game game-result]
  (print-board (:board game))
  (if (true? game-result)
    (println "Stalemate!")
    (if (:type game-result)
      (if (= "human" (:type game-result))
        (println "You won!")
        (if (= "computer" (:type game-result))
          (println "Computer won!")))))
  (println "Press any key to quit.")
  (read-line))

(defn play
  "Plays a game of tic tac toe using the CLI"
  []
  (let [player-human (create-human-player get-input-while)
        player-computer (create-computer-player get-computer-move)
        game (start-game [player-human player-computer] (create-board))]
    (loop [game-in-progress (take-turn game)]
      (let [game-result (is-game-over game-in-progress)]
        (if (not game-result)
          (recur (take-turn game-in-progress))
          (handle-end-game game-in-progress game-result))))))
