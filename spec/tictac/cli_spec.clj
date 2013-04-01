(ns tictac.cli-spec
  (:use speclj.core
        tictac.cli
        tictac.core))

(def fake-input-result (atom ""))
(def fake-input (fn [& args] @fake-input-result))

(def player-human    {:type "human"    :game-piece :O :input fake-input})
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

(describe "CLI"
  (it "gets a player's game-piece"
    (reset! fake-input-result "X")
    (let [updated-game (ask-player-for-piece game player-human)]
      (should= :X (:game-piece ((:players updated-game) 0)))))
  (it "ignores casing on the input"
    (reset! fake-input-result "o")
    (let [updated-game (ask-player-for-piece game player-human)]
      (should= :O (:game-piece ((:players updated-game) 0)))))

  (it "selects the left over piece for the computer player"
    (let [alternate-game (set-player-piece game player-human :X)
          updated-game (ask-player-for-piece alternate-game player-computer)]
      (should= :O (:game-piece ((:players updated-game) 1)))))

  (it "should create a position from a player's move"
    (reset! fake-input-result "5")
    (should= [1 1] (ask-player-for-move player-human)))

  (it "lets players make moves"
    (reset! fake-input-result "2")
    (let [board (assoc-in (:board game-human) [0 1] :O)]
      (should= board (:board (take-turn game-human)))))
  (it "automates the computer's turn"
    (let [board (assoc-in (:board game-computer) [1 1] :X)]
      (should= board (:board (take-turn game-computer)))))
  (it "updates the last move"
    (reset! fake-input-result "2")
    (let [last-turn (assoc-in (:turn game-human) [:position] [0 1])]
      (should= last-turn (:last-turn (take-turn game-human)))))
  (it "alternates the next player turn"
    (let [turn (assoc-in (:turn game-computer) [:player] player-human)]
      (should= turn (:turn (take-turn game-computer)))))
  )

(run-specs)