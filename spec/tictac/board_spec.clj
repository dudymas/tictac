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
  (it "shows the next open postion on a row"
    (let [row [:X nil nil]
          row-type :col-1]
      (should= [1 1] (get-open-position row row-type))))
  (it "detects when no more moves can be made"
    (should= true (board-filled finished-board)))
  (it "detects when moves can still be made"
    (should= false (board-filled unfinished-board)))

  (it "gets adjacent pieces"
    (let [col-0 '(:O :X :O)
          row-1 '(:X nil :O)
          diag  '(:O nil :X)]
    (should= col-0 (get-adjacent-pieces unfinished-board :col-0))
    (should= diag  (get-adjacent-pieces unfinished-board :diagonal-upper-left))
    (should= row-1 (get-adjacent-pieces unfinished-board :row-1))))

  (it "lists all adjacent rows for a given position"
    (let [rows [:row-2 :col-0 :diagonal-upper-right]]
      (should= rows (get-adjacent-rows unfinished-board [2 0])))))

(run-specs)