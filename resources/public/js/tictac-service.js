angular.module('tictac-services',['tictac-core'])
	.factory('ComputerMove', function(CurrentGame, $http){
		var ComputerMoveFactory = function ComputerMoveFactory () {
			return $http.post('/move', CurrentGame).then(function(res) {return res.data;});
		};
		return ComputerMoveFactory;
	})
	.service('DetectWin', function($http){
		return null;
	});