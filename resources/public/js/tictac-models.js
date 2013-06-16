angular.module('tictac-models',[])
	.factory("Player", function() {
		var Player = function Player () {};
		Player.create = function(piece, type) {
			var result = new Player();
			result["game-piece"] = piece;
			result.type = type;
			return result;
		};
		return Player;
	})
	.factory("Turn", function() {
		var Turn = function Turn () {};
		Turn.create = function() {
			return {player: null};
		};
		return Turn;
	})
	.factory("Row", function() {
		var Row = function Row () {};
		Row.create = function(rowLength) {
			var result = [];
			while(result.length < rowLength)
				result.push(null);
			return result;
		};
		return Row;
	})
	.factory("Board", function(Row) {
		var Board = function Board () {};
		Board.update = function(piece, pos) {
			if (!this[pos[0]][pos[1]])
				this[pos[0]][pos[1]] = piece;
		};
		Board.clear = function() {
			for (var i = 0; i < this.length; i++) {
				for (var j = 0; j < this[i].length; j++) {
					this[i][j] = null;
				};
			};
		};
		Board.create = function(rowCount, rowLength) {
			var result = [];
			while(result.length < rowCount)
				result.push(Row.create(rowLength));
			result.update = Board.update;
			result.clear = Board.clear;
			return result;
		};
		return Board;
	})
	.factory("Game", function(Turn) {
		var Game = function Game () {};
		Game.create = function(players, board) {
			var result = new Game();
			result.players = [];
			for (var playerIdx = 0; playerIdx < players.length; playerIdx++)
				result.players.push(players[playerIdx]);
			result.board = board;
			return result;
		};
		Game.prototype.reset = function() {
			this.board.clear();
			this.turn = Turn.create();
			this.turn.player = this.players[0];
		};
		return Game;
	});