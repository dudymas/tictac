(ns tictac.cli-spec
  (:use speclj.core
        tictac.cli
        tictac.core))

(defn make-phony-input
  [inp]
  (if (vector? inp)
    (let [results (atom inp)]
      (fn [& args]
        (let [result (first @results)]
          (swap! results rest)
          (println result)
          result)))
    (fn [& args] (println inp) inp)))

(def player-human-nilpiece {:type "human" :game-piece nil :input nil})

(def player-human    {:type "human"    :game-piece :O :input nil})
(def player-computer {:type "computer" :game-piece :X})

(def game {
  :players [player-human player-computer]
  :turn {:player player-human}
  })

(def game-human {
  :board [[nil nil nil]
          [nil nil nil]
          [nil nil nil]]
  :players [player-human player-computer]
  :turn {:player player-human}
  })

(def game-computer {
  :board [[nil nil nil]
          [nil nil nil]
          [nil nil nil]]
  :players [player-human player-computer]
  :turn {:player player-computer}
  })

(def game-human-nilpiece {
  :board [[nil nil nil]
          [nil nil nil]
          [nil nil nil]]
  :players [player-human-nilpiece player-computer]
  :turn {:player player-human-nilpiece}
  })

(describe "CLI"
  (tags :cli)

  (context "display-board"
    (it "displays the game board"
      (let [display [[1 2 3][4 "O" 6][7 8 9]]
            board   [[nil nil nil][nil :O nil][nil nil nil]]]
        (should= display (display-board board)))))

  (context "get-input-while"
    (tags :cli-input)
    (it "filters input using a filter function"
      (should= "o" (get-input-while #(= "o" %) "" (make-phony-input ["x" "i" "o"])))))

  (context "ask-player-for-piece"
    (tags :cli-input :cli-init)
    (it "gets a player's game-piece"
      (let [player (assoc-in player-human [:input] (make-phony-input "X"))
            updated-game (ask-player-for-piece game player)]
        (should= :X (:game-piece ((:players updated-game) 0)))))
    (it "ignores casing on the input"
      (let [player (assoc-in player-human [:input] (make-phony-input "o"))
            updated-game (ask-player-for-piece game player)]
        (should= :O (:game-piece ((:players updated-game) 0)))))
    (it "selects the left over piece for the computer player"
      (let [alternate-game (set-player-piece game player-human :X)
            updated-game (ask-player-for-piece alternate-game player-computer)]
        (should= :O (:game-piece ((:players updated-game) 1))))))

  (context "ask-player-for-move"
    (tags :cli-input :cli-move)
    (it "should create a position from a player's move"
      (let [player (assoc-in player-human [:input] (make-phony-input "5"))]
        (should= [1 1] (ask-player-for-move (:board game) player))))
    (it "does not allow players to move into taken spot"
      (let [mock (fn [f & args](first (filter f ["2" "3"])))
            player {:game-piece :O :input mock}
            board (assoc-in (:board game-human) [0 1] :O)]
        (should-not= [0 1] (ask-player-for-move board player)))))

  (context "take-turn"
    (tags :cli-input :cli-turn)
    (it "lets players make moves"
      (let [game (assoc-in game-human [:turn :player :input] (make-phony-input "2"))
            board (assoc-in (:board game-human) [0 1] :O)]
        (should= board (:board (take-turn game)))))
    (it "automates the computer's turn"
      (let [board (assoc-in (:board game-computer) [1 1] :X)]
        (should= board (:board (take-turn game-computer)))))
    (it "updates the last move"
      (let [game (assoc-in game-human [:turn :player :input] (make-phony-input "2"))
            last-turn (assoc-in (:turn game) [:position] [0 1])]
        (should= last-turn (:last-turn (take-turn game)))))
    (it "alternates the next player turn"
      (let [turn (assoc-in (:turn game-computer) [:player] player-human)]
        (should= turn (:turn (take-turn game-computer)))))
    (it "saves the player's choice of piece if it was nil"
      (let [game  (assoc-in
                    game-human-nilpiece
                    [:turn :player :input]
                    (make-phony-input ["O" "3"]))
            updated-game (take-turn game)]
        (should= :O (:game-piece (:player (:last-turn updated-game))))
        (should= :O (:game-piece ((:players updated-game) 0)))))))

(run-specs)