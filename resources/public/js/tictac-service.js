angular.module('tictac-services',['tictac-core'])
	.factory('ComputerMove', function(CurrentGame, $http){
		var ComputerMoveFactory = function ComputerMoveFactory () {
			return $http.post('/move', CurrentGame)
				.then(function(res) {return res.data;});
		};
		return ComputerMoveFactory;
	})
	.factory('DetectWin', function($http, CurrentGame){
		var DetectWinFactory = function DetectWinFactory () {
			return $http.post('/detect-win', CurrentGame)
				.then(function(res) {return res.data});
		}
		return DetectWinFactory;
	});