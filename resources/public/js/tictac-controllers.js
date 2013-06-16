angular.module('tictac-controllers', ['tictac-core', 'tictac-services'])
	.controller('boardCtrl', function($scope, CurrentGame) {
		$scope.board = CurrentGame.board;
		$scope.turn = CurrentGame.turn;
	});