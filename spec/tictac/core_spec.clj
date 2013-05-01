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

(def finished-game {
  :board finished-board
  :last-turn {:player player-computer :position [1 1]}})

(def unfinished-game {
  :board unfinished-board
  :last-turn {:player player-human :position [2 2]}})

(describe "tictac core"
  (tags :core)

  (context "create-board"
    (it "creates a game board"
      (should (create-board))))

  (context "start-game"
    (it "creates a game"
      (should (start-game [player-human player-computer] (create-board))))
    (it "lets human player take first move"
      (should= player-human (->> (create-board)
                           (start-game [player-human player-computer])
                           (:turn)
                           (:player)))))

  (context "get-player-idx"
    (it "gets the index of a player"
      (let [game (start-game [player-human player-computer] (create-board))]
        (should= 1 (get-player-idx game "computer"))
        (should= 0 (get-player-idx game "human")))))

  (context "set-player-piece"
    (it "sets the game piece for a player"
      (let [p1 (create-human-player)
            p2 (create-computer-player)
            game (start-game [p1 p2] (create-board))
            game-updated (-> (assoc-in game [:players 1 :game-piece] :X))]
        (should= game-updated (set-player-piece game p2 :X))))
    (it "sets the game piece for the current players turn"
      (let [p1 (create-human-player)
            p2 (create-computer-player)
            game (start-game [p1 p2] (create-board))
            game-updated (-> (assoc-in game [:players 0 :game-piece] :X)
                             (assoc-in      [:turn :player :game-piece] :X))]
        (should= game-updated (set-player-piece game p1 :X)))))

  (context "detect-win"
    (it "detects a winner"
      (let [winning-computer (assoc-in player-computer [:winning-row] :diagonal-upper-right)
            winning-human    (assoc-in player-human [:winning-row] :row-0)]
        (should= winning-computer (detect-win winning-game-x))
        (should= winning-human (detect-win winning-game-o))))
    (it "detects when no one has won yet"
      (should= nil (detect-win unfinished-game))))

  (context "is-game-over"
    (it "detects when the game is over"
      (should= true (is-game-over finished-game)))
    (it "detects when the game is not yet over"
      (should= nil (is-game-over unfinished-game)))))

(run-specs)