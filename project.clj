(defproject tictac "0.1.0-SNAPSHOT"
  :description "tictac : Unbeatable Tic Tac Toe in Clojure"
  :url "http://github.com/dudymas/tictac"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]]

  :profiles {
    :dev {
      :dependencies [[speclj "2.5.0"]]}}

  :plugins [[speclj "2.5.0"]]
  :test-paths ["spec/"]
  :main tictac.cli)
