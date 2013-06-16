angular.module('tictac-controllers', ['tictac-core', 'tictac-services'])
	.controller('boardCtrl', function($scope, CurrentGame) {
		$scope.board = CurrentGame.board;
		$scope.turn = CurrentGame.turn;
	})
	.controller('gameCtrl', function($scope, CurrentGame) {
		$scope.reset = function() {
			CurrentGame.reset();
		};
	})
	.controller('rowCtrl', function($scope, $MakeMove, CurrentGame) {
		$scope.setPiece = function(idx) {
			var pos = [$scope.$index, idx];
			$MakeMove(CurrentGame.turn.player, pos);
		};
	})
	.controller('winIndicatorCtrl', function($scope) {

	});