(ns tictac.bestmove
  (:use tictac.board))

(defn get-row-status
  "Returns :threat if a row is a threat to the given piece.
   Returns :win if winning. Else nil"
  ([row]
    (if (= :contested (get-row-status row :O))
      :contested)) ;;this can only be good for just getting contested rows
  ([row piece]
    (let [current-pieces (set (filter #(not(nil? %)) row))
          open-spot-count (count (filter #(nil? %) row))]
      (if (= 1 open-spot-count)
        (if (= (count current-pieces) 1)
          (if (.contains current-pieces piece)
            :win
            :threat))
        (if (= 2 open-spot-count)
          :contested)))))

(defn sort-rows
  "Sorts rows by their ability to contest for wins. For now, mostly just puts
  diagonals before horizontal or vertical rows."
  [rows]
  (concat
    (filter #(.contains [:diagonal-upper-left :diagonal-upper-right] %) rows)
    (filter #(.contains [:row-0 :row-1 :row-2 :col-0 :col-1 :col-2] %) rows)))

(defn sort-contested-positions
  "Given a list of positions, returns the most contested one."
  [board positions]
  (let [count-contested-rows
          (fn [p]
            (count
              (keep identity
                (map get-row-status
                  (map #(get-adjacent-pieces board %) (get-adjacent-rows p))))))]
    (sort-by count-contested-rows positions)))

(defn threaten-position
  "Given an open spot, return the most contested position that can be used to
  threaten that position. Otherwise (if no threats can be made) return nil."
  [board piece position]
  (if (get-in board position)
    nil ;can't threaten!
    (let [adj-rows (get-adjacent-rows position)
          test-board (assoc-in board position :_);fill the position
          row-with-room #(.contains (get-adjacent-pieces test-board %) nil)
          can-row-threaten #(-> (get-adjacent-pieces test-board %) (.contains piece))
          get-threat #(-> (get-adjacent-pieces test-board %) (get-open-positions %) (first))]
      (last
        (sort-contested-positions board
          (map get-threat
            (filter can-row-threaten
              (filter row-with-room adj-rows))))))))

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
              (get-open-positions adj-pieces row))
            (get-open-positions adj-pieces row)))))))

(defn get-best-move
  "Determines from the current player turn what the best move should be.
   Returns nil if there is no best move."
  [game]
  ;;look at last move if there is one
  (if (and (:last-turn game) (get-in game [:board 1 1]))
    (let [last-position (:position (:last-turn game))
          piece-in-play (:game-piece (:player (:turn game)))
          adj-rows (get-adjacent-rows last-position)
          board (:board game)]
      (or
        (some ;;see if there are any wins we can take this very second
          #(first (filter-positions board % piece-in-play [:win]))
          (map get-adjacent-rows (get-piece-locations board nil)))
      (first (filter-positions board adj-rows piece-in-play [:win :threat]))))
    [1 1]))

(defn get-best-threat
  "Tries to predict which threat has tbe best outcome and return it. Best threats
  force the opponent to choose a less contested row to block a threat."
  [game]
  (let [board (:board game)
        piece-in-play (get-in game [:turn :player :game-piece])
        open-positions (get-piece-locations board nil)
        bad-move (first (sort-contested-positions board open-positions))]
    (threaten-position board piece-in-play bad-move)))

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
      (let [adj-rows (get-adjacent-rows position)
            find-pos #(last (sort-contested-positions
                                    board
                                    (filter-positions board adj-rows piece-in-play %)))]
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
    (delay (get-best-threat game))
    (delay (get-offensive-move game))
    (delay (take 1 (get-piece-locations (:board game) nil)))]))
