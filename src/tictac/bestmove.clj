(ns tictac.bestmove
  (:use tictac.board))

(defn get-row-status
  "Returns :threat if a row is a threat to the given piece. 
   Returns :win if winning. Else nil"
  [row piece]
  (let [current-pieces (set (filter #(not(nil? %)) row))
        open-spot-count (count (filter #(nil? %) row))
        ]
    (if (= 1 open-spot-count)
      (if (= (count current-pieces) 1)
        (if (.contains current-pieces piece)
          :win
          :threat)))))

(defn get-best-move
  "Determines from the current player turn what the best move should be. 
   Returns nil if there is no best move."
  [game]
  ;;look at last move if there is one
  (if (:last-turn game)
    (let [last-position (:position (:last-turn game))
          piece-in-play (:game-piece (:player (:turn game)))
          adj-rows (get-adjacent-rows (:board game) last-position)]
      ((fn [[row & rows-remaining]]
          (let [ adj-pieces (get-adjacent-pieces (:board game) row)
                row-status (get-row-status adj-pieces piece-in-play)]
            (if (nil? row-status)
              (if rows-remaining
                (recur rows-remaining))
              (get-open-position adj-pieces row)))) 
        adj-rows))
    [1 1]))

(defn get-computer-move
  "Attempts to make the best next move, or just picks an available spot if there
  is no best move and the center is taken."
  [game]
  (let [best (get-best-move game)]
    (if best
      best
      (if (nil? (((:board game) 1) 1))
        [1 1]
        ((fn [[row & rows-remaining]]
          (let [adj-pieces (get-adjacent-pieces (:board game) row)
                choice (get-open-position adj-pieces row)]
            (if choice
              choice
              (if rows-remaining
                (recur rows-remaining)))))
          [:row-0 :row-1 :row-2])))))
