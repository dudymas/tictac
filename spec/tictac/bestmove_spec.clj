(ns tictac.bestmove-spec
  (:use speclj.core
        tictac.bestmove))

(def player-human    {:type "human"    :game-piece :O})
(def player-computer {:type "computer" :game-piece :X})

(def empty-game {
  :board [[nil nil nil]
          [nil nil nil]
          [nil nil nil]]
  :turn {:player player-human :position nil}
  :last-turn nil})

(def game-with-center-filled {
  :board [[nil nil nil]
          [nil :O  nil]
          [nil nil nil]]
  :turn {:player player-computer :position nil}
  :last-turn {:player player-human :position [1 1]}})

(def threatened-board-o
  [[nil :X  :O ]
   [nil :X  nil]
   [nil nil nil]])

(def threatened-game-o {
  :board threatened-board-o
  :turn {:player player-human :position nil}
  :last-turn {:player player-computer :position [0 1]}})

(def open-game-board
  [[nil nil nil]
   [nil :X  :O ]
   [:O  nil nil]])

(def open-game {
  :board open-game-board
  :turn {:player player-computer}
  :last-turn {:player player-human :position [1 2]}})

(def open-game-board-x
  [[nil nil nil]
   [:O  :X  :O ]
   [nil nil nil]])

(def open-game-x {
  :board open-game-board-x
  :turn {:player player-computer}
  :last-turn {:player player-human :position [1 2]}})

(def open-game-board-o
  [[:X  :X  nil]
   [:O  :X  :O ]
   [nil :O  :O]])

(def open-game-o {
  :board open-game-board-o
  :turn {:player player-computer}
  :last-turn {:player player-human :position [2 2]}})

(def game-with-one-opening {
  :board [[:O  :X  :O ]
          [:O  :X  nil]
          [:X  :O  :X]]
  :turn {:player player-human :position nil}
  :last-turn {:player player-computer :position [0 1]}})

(describe "move status"
  (tags :bm :bm-status)

  (context "get-row-status"
    (it "can tell if a row is a threat"
      (let [row [:X nil :X]]
        (should= :threat (get-row-status row :O))))
    (it "can tell if a row is a win"
      (let [row [:X nil :X]]
        (should= :win (get-row-status row :X))))
    (it "can tell if a row is contested"
      (let [row [:X nil nil]]
        (should= :contested (get-row-status row :X))))

    (it "can ignore a row that has nothing open"
      (let [row [:X :O :X]]
        (should= nil (get-row-status row :X))))
    (it "can ignore a row that has too many open spots"
      (let [row [nil nil nil]]
        (should= nil (get-row-status row :X)))))

  (context "filter-positions"
    (it "filters positions by row status"
      (should (.contains
                [[2 1] [2 2]]
                (filter-positions open-game-board [:row-0 :row-1 :row-2] :O [:win :contested]))))
    (it "prefers lower index statuses over higher ones"
      (should= [2 1] (filter-positions
                        threatened-board-o
                        [:col-0 :col-1 :col-2]
                        :O
                        [:win :threat :contested nil])))))

(describe "defense (stop threats/pro-action)"
  (context "get-best-move"
    (tags :bm :bm-best)
    (it "defaults to center position"
      (should= [1 1] (get-best-move empty-game)))
    (it "stops threats to the current turn"
      (let [bestmove [2 1]]
        (should= bestmove (get-best-move threatened-game-o))))
    (it "should still look for a win before blocking a threat"
      (should= [0 2] (get-best-move open-game-o)))
    (it "returns nil if there is no best move"
      (should-not (get-best-move open-game-x)))))

(describe "offense (attempting to win)"
  (tags :bm :bm-offense)
  (context "get-offensive-move"
    (it "gets rows that are contested"
      (let [game (-> (assoc-in threatened-game-o [:board 1 1] :O)
                     (assoc-in                   [:last-turn :position] [1 1])
                     (assoc-in                   [:last-turn :player] player-human)
                     (assoc-in                   [:turn :player] player-computer))]
        (should (.contains
                  [[1 0][1 2]]
                  (get-offensive-move game)))))
    (it "prefers contested rows that are going to turn into wins"
      (let [game (-> (assoc-in threatened-game-o [:board 1 1] nil)
                     (assoc-in                   [:last-turn :position] [0 2])
                     (assoc-in                   [:last-turn :player] player-human)
                     (assoc-in                   [:turn :player] player-computer))]
        (should (.contains
                  [[1 1][2 1]]
                  (get-offensive-move game)))))
    (it "prefers wins over contested rows"
      (let [game (-> (assoc-in threatened-game-o [:last-turn :player] player-human)
                     (assoc-in                   [:turn :player] player-computer))]
        (should= [2 1] (get-offensive-move game))))))

(describe "computer"
  (context "get-computer-move"
    (tags :bm :bm-computer)
    (it "takes the best move"
      (should= [1 1] (get-computer-move empty-game)))
    (it "guesses when there is no best move"
      (should-not (nil? (get-computer-move game-with-one-opening))))
    (it "picks a move other than center if it is taken"
      (should-not (.contains [nil [1 1]] (get-computer-move game-with-center-filled))))
    (it "takes a win before guessing"
      (let [threatened-game-o (->
          (assoc-in threatened-game-o [:turn :player] player-computer)
          (assoc-in                   [:last-turn] {:player player-human :position [0 2]}))]
        (should= [2 1] (get-computer-move threatened-game-o))))
    (it "attempts to threaten with the next turn"
      (should (.contains #{[0 0] [0 1] [2 1] [2 2]} (get-computer-move open-game))))))

(run-specs)