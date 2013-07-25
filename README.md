# tictac

A Clojure library designed to implement a basic unbeatable tic-tac-toe game.

## Usage

Both the CLI and Web-UI are working now. You'll need:
* Clojure 1.5.1
* [leiningen 2](https://github.com/technomancy/leiningen) or greater.

To run (CLI):

```bash
$ git clone http://github.com/dudymas/tictac.git
$ cd tictac
$ lein run
```

To run (Web):

```bash
$ git clone http://github.com/dudymas/tictac.git
$ cd tictac
$ lein ring server
```

## Play

You take the first move with game-piece 'O'.
* Command Line :
 * Pick open positions using the numbers 1-9
 * When the game is won, the game exits and the player is back at the command prompt.
* Web UI :
 * Click on open positions to place a piece.
 * When the game is won, clicking the 'reset' button will clear the board. No more moves can be made until the board is cleared.

## Future plans

At this time the human player is always an 'O'. Plans are to allow more characters/pieces. Also, the first player is always the human player at this point. Later, the starting player will be chooseable.

## License

Copyright © 2013 Jeremy White

Distributed under the Eclipse Public License, the same as Clojure.
