(ns tictac.core-spec
  (:use speclj.core
        tictac.core))

(describe "tictac core"
  (it "creates a game board"
    (should (create-board))))

(run-specs)