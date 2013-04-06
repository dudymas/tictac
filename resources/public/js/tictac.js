
angular.module("tictac", [])
	.factory("Player", function (){
		var player = {piece : "O"};
		var Player = function Player () {};
		Player.prototype.getPiece = function() {
			return player.piece;//make a ui call later if this isn't set yet
		};
		Player.prototype.getData = function() {
			return player;
		};
		return new Player();
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

function rowCtrl ($scope, Player) {
	$scope.setPiece = function(pos) {
		var piece = $scope.row[pos];
		if (piece == null)
			piece = Player.getPiece();
		else if (piece == "O")
			piece = "X";
		else
			piece = null;
		$scope.row[pos] = piece;
	};
}

function debugCtrl ($scope, Board, Player) {
	//show info about our state
	$scope.data = {};
	$scope.data.board = Board.getData();
	$scope.data.player = Player.getData();
}
