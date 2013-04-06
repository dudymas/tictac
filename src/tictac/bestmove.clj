(ns tictac.bestmove
  (:use tictac.board))

(defn get-row-status
  "Returns :threat if a row is a threat to the given piece.
   Returns :win if winning. Else nil"
  [row piece]
  (let [current-pieces (set (filter #(not(nil? %)) row))
        open-spot-count (count (filter #(nil? %) row))]
    (if (= 1 open-spot-count)
      (if (= (count current-pieces) 1)
        (if (.contains current-pieces piece)
          :win
          :threat))
      (if (= 2 open-spot-count)
        :contested))))

(defn sort-rows
  "Sorts rows by their ability to contest for wins. For now, mostly just puts
  diagonals before horizontal or vertical rows."
  [rows]
  (concat
    (filter #(.contains [:diagonal-upper-left :diagonal-upper-right] %) rows)
    (filter #(.contains [:row-0 :row-1 :row-2 :col-0 :col-1 :col-2] %) rows)))

(defn filter-positions
  "Given a list of acceptable statuses and rows on a board for a piece,
  Returns the first acceptable position to be used. Prefers first status"
  [board rows piece acceptable-statuses]
  (loop [[row & rows-remaining] (sort-rows rows)]
    (let [ adj-pieces (get-adjacent-pieces board row)
          row-status (get-row-status adj-pieces piece)]
      (if (not (.contains acceptable-statuses row-status))
        (if rows-remaining
          (recur rows-remaining))
        (let [status-idx (.indexOf acceptable-statuses row-status)]
          (if (> status-idx 0) ;can we do better?
            (or
              (filter-positions board rows piece (take status-idx acceptable-statuses))
              (get-open-position adj-pieces row))
            (get-open-position adj-pieces row)))))))

(defn get-best-move
  "Determines from the current player turn what the best move should be.
   Returns nil if there is no best move."
  [game]
  ;;look at last move if there is one
  (if (and (:last-turn game) (get-in game [:board 1 1]))
    (let [last-position (:position (:last-turn game))
          piece-in-play (:game-piece (:player (:turn game)))
          adj-rows (get-adjacent-rows (:board game) last-position)
          board (:board game)]
      (or
        (some ;;see if there are any wins we can take this very second
          #(filter-positions board % piece-in-play [:win])
          (map
            #(get-adjacent-rows board %)
            (get-piece-locations board nil)))
      (filter-positions board adj-rows piece-in-play [:win :threat])))
    [1 1]))

(defn get-offensive-move ;;TODO: add personal fouls though... not in my house
  "Tries to choose either winning moves or moves to gain a contested row."
  ([game]
    (let [piece-in-play (:game-piece (:player (:turn game)))
          last-position (:position (:last-turn game))
          board         (:board game)
          positions (concat (get-piece-locations board piece-in-play) [last-position])]
    (get-offensive-move board positions piece-in-play)))
  ([board positions piece-in-play]
    (loop [contested-position nil
           [position & positions-remaining] positions]
      (let [adj-rows (get-adjacent-rows board position)
            find-pos #(filter-positions board adj-rows piece-in-play %)]
        (let [[status position]
                (some #(when (last %) %) [
                  [:win       (find-pos [:win])]
                  [:contested contested-position]
                  [:contested (find-pos [:contested])]])]
          (if (= status :win)
            position
            (if positions-remaining
              (recur position positions-remaining)
              position)))))))

(defn get-computer-move
  "Attempts to make the best next move, or just picks an available spot if there
  is no best move and the center is taken."
  [game]
  (some #(do @%) [ ;;lazy execute these until we get something
    (delay (get-best-move game))
    (delay (get-offensive-move game))
    (delay (take 1 (get-piece-locations (:board game) nil)))]))
