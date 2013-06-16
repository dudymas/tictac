angular.module('tictac-controllers', ['tictac-core', 'tictac-services'])
	.controller('boardCtrl', function($scope, CurrentGame) {
		$scope.board = CurrentGame.board;
		$scope.turn = CurrentGame.turn;
	})
	.controller('gameCtrl', function($scope) {
		
	})
	.controller('rowCtrl', function($scope) {
		
	})
	.controller('winIndicatorCtrl', function($scope) {
		
	});