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
  :last-turn nil
  })

(def threatened-game-o {
  :board [[nil :X  :O ]
          [nil :X  nil]
          [nil nil nil]]
  :turn {:player player-human :position nil}
  :last-turn {:player player-computer :position [0 1]}
  })

(def game-with-one-opening {
  :board [[:O  :X  :O ]
          [:O  :X  nil]
          [:X  :O  :X]]
  :turn {:player player-human :position nil}
  :last-turn {:player player-computer :position [0 1]}
  })

(describe "bestmove"
  (it "defaults to center position"
    (should= [1 1] (get-best-move empty-game)))
  (it "returns nil if there is no best move"
    (should= nil (get-best-move game-with-one-opening)))

  (it "can tell if a row is a threat"
    (let [row [:X nil :X]]
      (should= :threat (get-row-status row :O))))
  (it "can tell if a row is a win"
    (let [row [:X nil :X]]
      (should= :win (get-row-status row :X))))

  (it "can ignore a row that has nothing open"
    (let [row [:X :O :X]]
      (should= nil (get-row-status row :X))))
  (it "can ignore a row that has too many open spots"
    (let [row [:X nil nil]]
      (should= nil (get-row-status row :X)))
    (let [row [nil nil nil]]
      (should= nil (get-row-status row :X))))

  (it "stops threats to the current turn"
    (let [bestmove [2 1]]
      (should= bestmove (get-best-move threatened-game-o)))))

(run-specs)