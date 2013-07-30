(ns tictac.board)

(def computer-default-piece :X)

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

(defn get-open-positions
  "Returns a list of open board positions on a row given the row and the row type"
  ([row-type] ;overload useful for just getting position of a row index
    (get-open-positions [nil nil nil] row-type))
  ([row row-type]
    (let [idxs (keep identity (map #(if %1 nil %2) row (range 3)))]
      (if (>= (count idxs) 0)
        (case row-type
            :row-0 (map [[0 0][0 1][0 2]] idxs)
            :row-1 (map [[1 0][1 1][1 2]] idxs)
            :row-2 (map [[2 0][2 1][2 2]] idxs)
            :col-0 (map [[0 0][1 0][2 0]] idxs)
            :col-1 (map [[0 1][1 1][2 1]] idxs)
            :col-2 (map [[0 2][1 2][2 2]] idxs)
            :diagonal-upper-left  (map [[0 0][1 1][2 2]] idxs)
            :diagonal-upper-right (map [[0 2][1 1][2 0]] idxs)
            )))))

(defn get-position-by-idx
  "Gets the position of a move by its index"
  [idx]
  [({1 0   2 0   3 0
     4 1   5 1   6 1
     7 2   8 2   9 2} idx)   ;;pick a row
  ({ 1 0   2 1   3 2
     4 0   5 1   6 2
     7 0   8 1   9 2} idx)])

(defn get-piece-locations
  "Returns an array of positions for each move of a given piece on a given board"
  [board piece]
  (let [move (atom 0)
        find-piece #(do (if (= piece %) (get-position-by-idx @move) ))
        incr-board-position #(do (swap! move inc) (find-piece %))]
    (filter #(not(nil? %))
      (reduce concat
        (map
          #(map incr-board-position %) board)))))

(defn is-move-legal
  "Tests if a given move is legal or not. Moves are legal if they are made on open (nil) positions."
  [board move]
  (let [position (get-position-by-idx move)
       legal-positions (get-piece-locations board nil)]
    (.contains legal-positions position)))

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
      (list ((game-board 0) 2) ((game-board 1) 1) ((game-board 2) 0))))

(defn get-adjacent-rows
  "Returns sequence of rows of adjacent board positions for a given board and position"
  [[row col]]
  (case row;;which row are we at
    0 (case col
      0 [:diagonal-upper-left :col-0 :row-0]
      1 [:row-0 :col-1]
      2 [:diagonal-upper-right :col-2 :row-0])
    1 (case col
      0 [:row-1 :col-0]
      1 [:diagonal-upper-left :diagonal-upper-right :row-1 :col-1]
      2 [:row-1 :col-2])
    2 (case col
      0 [:diagonal-upper-right :col-0 :row-2]
      1 [:row-2 :col-1]
      2 [:diagonal-upper-left :col-2 :row-2])))
