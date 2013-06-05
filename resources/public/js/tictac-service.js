angular.module('tictac-services',[])
	.factory('ComputerMove', function($http){
		var ComputerMoveFactory = function ComputerMoveFactory () {
			$http.post('/move', null);
		};
		return ComputerMoveFactory;
	})
	.service('DetectWin', function($http){
		return null;
	});