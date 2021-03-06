(defproject tictac "0.1.0-SNAPSHOT"
  :description "tictac : Unbeatable Tic Tac Toe in Clojure"
  :url "http://github.com/dudymas/tictac"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [org.clojure/data.json "0.2.1"]]

  :profiles {
    :dev {
      :dependencies [[speclj "2.5.0"]]}}

  :plugins [[lein-ring "0.8.3"]
            [speclj "2.5.0"]]
  :test-paths ["spec/"]
  :main tictac.cli

  :ring {:handler tictac.web/handler})