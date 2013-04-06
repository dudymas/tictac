
angular.module("tictac", [])
	.factory("Turn", function() {
		var turn = { player : null };
		var Turn = function Turn () {}
		Turn.prototype.getData = function() {return turn;}; 
		return new Turn();
	})
	.factory("Player", function (){
		var player = {piece : "O", type: "human"};
		var Player = function Player () {};
		Player.prototype.getData = function() {return player; };
		return new Player();
	})
	.factory("Computer", function() {
		var computer = {piece : "X", type: "computer", "is-on": true};
		var Computer = function Computer () {};
		Computer.prototype.getData = function() {return computer; };
		Computer.prototype.requestMove = function(game) {
			var row = Math.floor(Math.random()*3);
			var col = Math.floor(Math.random()*3);
			return [row,col];
		};
		return new Computer();
	})
	.factory("Game", function(Player,Computer,Board, Turn) {
		var humanPlayer = Player.getData();
		var computerPlayer = Computer.getData();
		var game = {
			"last-turn" : {},
			board       : Board.getData(),
			turn        : Turn.getData(),
			players		: [humanPlayer, computerPlayer]
		};
		var Game = function Game () {
			//init
			if (!game.turn.player)
				game.turn.player = humanPlayer;
			if (!game["last-turn"].player)
				game["last-turn"].player = computerPlayer;
		}
		Game.prototype.getData = function() {return game; };
		Game.prototype.move = function(player, position) {
			if (humanPlayer === player && computerPlayer["is-on"]) {
				//don't actually cycle turns, just make another move
				var move = Computer.requestMove(game);
				game.board[move[0]][move[1]] = computerPlayer.piece;
			} else {
				game.turn.player = game["last-turn"].player;
				game["last-turn"] = {player: player, position: position };
			}
		};
		return new Game();
	})
	.factory("Board", function() {
		var board = [
		[null, null, null ],
		[null, null, null ],
		[null, null, null ]]
		var Board = function Board () {
		}
		Board.prototype.getData = function() {return board; };
		return new Board();
	});

function boardCtrl ($scope, Board, Turn) {
	$scope.board = Board.getData();
	$scope.turn = Turn.getData();
}

function rowCtrl ($scope, Turn, Game) {
	$scope.setPiece = function(pos) {
		var turn = Turn.getData();
		$scope.row[pos] = turn.player.piece;
		Game.move(turn.player, [$scope.$index, pos]);
	};
}

function computerCtrl ($scope, Computer) {
	$scope.computer = Computer.getData();
}

function debugCtrl ($scope, Player, Computer, Board, Game, Turn) {
	//show info about our state
	$scope.data = {};
	$scope.data.board = Board.getData();
	$scope.data.player = Player.getData();
	$scope.data.computer = Computer.getData();
	$scope.data.game = Game.getData();
	$scope.data.turn = Turn.getData();
}
