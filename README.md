# tictac

A Clojure library designed to implement a basic unbeatable tic-tac-toe game. Implemented on the web using AngularJS and vanilla javascript (I can try clojurescript if someone requests it.).

## Usage

You will need [leiningen 2](https://github.com/technomancy/leiningen) or greater. To run the CLI version:

```bash
$ git clone http://github.com/dudymas/tictac.git
$ cd tictac
$ lein run
```

To run the web-browser version (only tested in the latest chrome):

```bash
$ git clone http://github.com/dudymas/tictac.git
$ cd tictac
$ lein ring server 8888
```

By default the webpage starts without talking to the server. Use the checkboxes to mess with behaviour if you like.

The port can be anything you like (8888 is just an example). To run ring without spawning a web browser, you just run `lein ring server-headless 8888`.

## License

Copyright © 2013 Jeremy White

Distributed under the Eclipse Public License, the same as Clojure.
