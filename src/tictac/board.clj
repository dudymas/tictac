(ns tictac.board)

(defn board-filled
  "Detects if the game is over"
  [game-board]
  (if (->> game-board
          ;;flatten board and look for any nil items. This could be more efficient.
          ;;For larger game-boards, should use recur.
          (reduce concat)
          (reduce #(or %1 (nil? %2)) false))
    false
    true))

(defn get-open-position
  "Returns the first open board position on a row given the row and the row type"
  [row row-type]
  (let [idx (.indexOf row nil)]
    (case row-type
      :row-0 ([[0 0][0 1][0 2]] idx)
      :row-1 ([[1 0][1 1][1 2]] idx)
      :row-2 ([[2 0][2 1][2 2]] idx)
      :col-0 ([[0 0][1 0][2 0]] idx)
      :col-1 ([[0 1][1 1][2 1]] idx)
      :col-2 ([[0 2][1 2][2 2]] idx)
      :diagonal-upper-left  ([[0 0][1 1][2 2]] idx)
      :diagonal-upper-right ([[0 2][1 1][2 0]] idx)
      )))

(defn get-adjacent-pieces
  "Given a specific row, column, or diagonal, this returns a list of pieces"
  [game-board adjacency]
  (case adjacency
    :row-0 (game-board 0)
    :row-1 (game-board 1)
    :row-2 (game-board 2)
    :col-0 (reduce #(concat %1 [(%2 0)]) [] game-board)
    :col-1 (reduce #(concat %1 [(%2 1)]) [] game-board)
    :col-2 (reduce #(concat %1 [(%2 2)]) [] game-board)
    :diagonal-upper-left
      (list ((game-board 0) 0) ((game-board 1) 1) ((game-board 2) 2))
    :diagonal-upper-right
      (list ((game-board 0) 2) ((game-board 1) 1) ((game-board 2) 0))
    ))

(defn get-adjacent-rows
  "Returns sequence of rows of adjacent board positions for a given board and position"
  [game-board position]
  (case (position 0);;which row are we at
    0 (case (position 1)
      0 [:row-0 :col-0 :diagonal-upper-left]
      1 [:row-0 :col-1]
      2 [:row-0 :col-2 :diagonal-upper-right])
    1 (case (position 1)
      0 [:row-1 :col-0]
      1 [:row-1 :col-1 :diagonal-upper-left :diagonal-upper-right]
      2 [:row-1 :col-2])
    2 (case (position 1)
      0 [:row-2 :col-0 :diagonal-upper-right]
      1 [:row-2 :col-1]
      2 [:row-2 :col-2 :diagonal-upper-left])))

