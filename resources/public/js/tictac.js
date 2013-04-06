
angular.module("tictac", [])
	.factory("Player", function (){
		var player = {piece : "O", type: "human"};
		var Player = function Player () {};
		Player.prototype.getPiece = function() {
			return player.piece;//make a ui call later if this isn't set yet
		};
		Player.prototype.getData = function() {
			return player;
		};
		return new Player();
	})
	.factory("Computer", function() {
		var computer = {piece : "X", type: "computer"};
		var Computer = function Computer () {};
		Computer.prototype.getData = function() {
			return computer;
		};
		return new Computer();
	})
	.factory("Game", function(Player,Computer,Board) {
		var game = {
			"last-turn" : {player: Player   .getData()},
			board       : 		   Board    .getData(),
			turn        : {player: Computer .getData()}
		};
		game.players = [game.turn.player, game["last-turn"].player];
		var Game = function Game () {}
		Game.prototype.getData = function() {
			return game;
		};
		Game.prototype.move = function(player, position) {
			if (game["last-turn"].player === player) {
				game["last-turn"].position = position;
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
		Board.prototype.getData = function() {
			return board;
		};
		return new Board();
	});

function boardCtrl ($scope, Board) {
	$scope.board = Board.getData();
}

function rowCtrl ($scope, Player, Game) {
	$scope.setPiece = function(pos) {
		var piece = $scope.row[pos];
		if (piece == null)
			piece = Player.getPiece();
		else if (piece == "O")
			piece = "X";
		else
			piece = null;
		$scope.row[pos] = piece;
		Game.move(Player.getData(), [$scope.$index, pos]);
	};
}

function debugCtrl ($scope, Player, Computer, Board, Game) {
	//show info about our state
	$scope.data = {};
	$scope.data.board = Board.getData();
	$scope.data.player = Player.getData();
	$scope.data.computer = Computer.getData();
	$scope.data.game = Game.getData();
}
