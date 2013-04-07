
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
	.factory("Computer", function($http) {
		var computer = {piece : "X", type: "computer", "is-on": true, online: false};
		computer["status"] = "ready for request";
		var Computer = function Computer () {};
		Computer.prototype.getData = function() {return computer; };
		Computer.prototype.requestMove = function(game, cb) {
			if (computer.online){
				computer.status = "requesting";
				return $http.post('/move', game).success(function(d) {
					computer.status = "ready for request";
					if (typeof(cb) == "function") cb(d);
				}).error(function() {
					computer.status = "error!";
					computer.errors = arguments;//be argumentative
				});
			}
			var row = Math.floor(Math.random()*3);
			var col = Math.floor(Math.random()*3);
			if (typeof(cb) == "function") cb([row,col]);
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
		var swapTurn = function(player, position) {
				game.turn.player = game["last-turn"].player;
				game["last-turn"] = {player: player, position: position };
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
				setTimeout(function(){
					swapTurn(humanPlayer, position);
					Computer.requestMove(game,function waitComputerAjax(compPosition) {
						game.board[compPosition[0]][compPosition[1]] = computerPlayer.piece;
						swapTurn(computerPlayer, compPosition);
					});
				}, 0); //just do this after the caller has finished
				return true;
			} else if (computerPlayer === player && !computerPlayer["is-on"]) {
				swapTurn(player, position);
				return true;
			}
			return false;
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
		//only update the board row if the game allows a move
		if (Game.move(turn.player, [$scope.$index, pos]))
			$scope.row[pos] = turn.player.piece;
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
