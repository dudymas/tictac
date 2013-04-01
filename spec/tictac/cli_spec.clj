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
          result)))
    (fn [& args] inp)))

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
  (it "displays the game board"
    (let [display [[1 2 3][4 "O" 6][7 8 9]]
          board   [[nil nil nil][nil :O nil][nil nil nil]]]
      (should= display (display-board board))))

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
      (should= :O (:game-piece ((:players updated-game) 1)))))

  (it "should create a position from a player's move"
    (let [player (assoc-in player-human [:input] (make-phony-input "5"))]
      (should= [1 1] (ask-player-for-move player))))

  (it "lets players make moves"
    (let [game (assoc-in game-human [:turn :player :input] (make-phony-input "2"))
          board (assoc-in (:board game-human) [0 1] :O)]
      (should= board (:board (take-turn game)))))
  (it "automates the computer's turn"
    (let [board (assoc-in (:board game-computer) [1 1] :X)]
      (should= board (:board (take-turn game-computer)))))
  (it "updates the last move"
    (let [game (assoc-in game-human [:turn :player :input] (make-phony-input "2"))
          last-turn (assoc-in (:turn game-human) [:position] [0 1])]
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
      (should= :O (:game-piece ((:players updated-game) 0)))))
  )

(run-specs)