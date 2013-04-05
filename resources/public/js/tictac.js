
angular.module("tictac", [])
	.factory("Player", function (){
		var Player = function Player () {};
		var piece = "O";
		Player.prototype.getPiece = function() {
			return piece;//make a ui call later if this isn't set yet
		};
		return Player;
	});

function boardCtrl ($scope) {
	$scope.board = [
		[null,  null,  null ]
		[null,  null,  null ]
		[null,  null,  null ]];
}

function positionCtrl ($scope, Player) {
	$scope.piece = null;
	$scope.click = function() {
		if ($scope.piece == null)
			$scope.piece = Player.getPiece();
	};
}
