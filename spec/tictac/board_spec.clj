(ns tictac.board-spec
  (:use speclj.core
        tictac.board))

(def finished-board [
  [:O :O :X]
  [:X :X :O]
  [:O :O :X]])
(def unfinished-board [
  [:O nil :X]
  [:X nil :O]
  [:O nil :X]])

(describe "board"
  (tags :board)
  (context "get-open-positions"
    (it "shows the next open postion on a row"
      (let [row [:X nil nil]
            row-type :col-1]
        (should= #{[1 1] [2 1]} (set (get-open-positions row row-type))))))

  (context "board-filled"
    (it "detects when no more moves can be made"
      (should= true (board-filled finished-board)))
    (it "detects when moves can still be made"
      (should= false (board-filled unfinished-board))))

  (context "get-piece-locations"
    (it "gets locations of a given piece"
      (let[board [[nil :X  :O ]
                  [:O  :X  nil]
                  [:X  nil nil]]]
        (should= #{[1 1] [0 1] [2 0]} (set (get-piece-locations board :X))))))

  (context "is-move-legal"
    (it "returns true for legal moves"
      (should (is-move-legal unfinished-board 2))
      (should (is-move-legal unfinished-board 5))
      (should (is-move-legal unfinished-board 8)))
    (it "returns false for illegal moves"
      (should-not (is-move-legal unfinished-board 1))
      (should-not (is-move-legal unfinished-board 3))
      (should-not (is-move-legal unfinished-board 7))))

  (context "get-adjacent-pieces"
    (it "gets adjacent pieces"
      (let [col-0 '(:O :X :O)
            row-1 '(:X nil :O)
            diag  '(:O nil :X)]
      (should= col-0 (get-adjacent-pieces unfinished-board :col-0))
      (should= diag  (get-adjacent-pieces unfinished-board :diagonal-upper-left))
      (should= row-1 (get-adjacent-pieces unfinished-board :row-1)))))

  (context "get-adjacent-rows"
    (it "lists all adjacent rows for a given position"
      (let [rows #{:row-2 :col-0 :diagonal-upper-right}]
        (should= rows (set (get-adjacent-rows [2 0])))))))

(run-specs)