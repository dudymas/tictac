(ns tictac.core-spec
  (:use speclj.core
        tictac.core))

(def player-human {:type "human" :game-piece :O})
(def player-computer {:type "computer" :game-piece :X})

(def finished-board [
  [:O :O :X]
  [:X :X :O]
  [:O :O :X]])
(def unfinished-board [
  [:O nil :X]
  [:X nil :O]
  [:O nil :X]])

(def winning-game-o {
  :board [[:O  :O  :O]
          [nil :X  nil]
          [:X  :O  :X]]
  :last-turn {:player player-human :position [0 2]}})

(def winning-game-x {
  :board [[:O  :O  :X]
          [nil :X  nil]
          [:X  :O  :O]]
  :last-turn {:player player-computer :position [1 1]}})

(def unfinished-game {
  :board unfinished-board
  :last-turn {:player player-human :position [2 2]}})

(describe "tictac core"
  (it "creates a game board"
    (should (create-board)))
  (it "creates a game"
    (should (start-game [player-human player-computer] (create-board))))
  (it "lets human player take first move"
    (should= player-human (->> (create-board)
                         (start-game [player-human player-computer])
                         (:turn)
                         (:player))))

  (it "detects a winner"
    (should= player-computer (detect-win winning-game-x))
    (should= player-human (detect-win winning-game-o)))
  (it "detects when no one has won yet"
    (should= nil (detect-win unfinished-game))))

(run-specs)