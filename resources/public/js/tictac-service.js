angular.module('tictac-services',[])
	.factory('ComputerMove', function($http){
		var ComputerMoveFactory = function ComputerMoveFactory () {};
		return ComputerMoveFactory;
	})
	.service('DetectWin', function($http){
		return null;
	});