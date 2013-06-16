angular.module('tictac-services',['tictac-core'])
	.value('$DataPromise', function(ajaxPromise) {
		var handleResp = function(res) {return res.data;};
		return ajaxPromise.then(handleResp);
	})
	.factory('ComputerMove', function($http, CurrentGame, $DataPromise){
		var ComputerMoveFactory = function ComputerMoveFactory () {
			var promise = $http.post('/move', CurrentGame);
			return $DataPromise(promise);
		};
		return ComputerMoveFactory;
	})
	.factory('DetectWin', function($http, CurrentGame, $DataPromise){
		var DetectWinFactory = function DetectWinFactory () {
			var promise = $http.post('/detect-win', CurrentGame);
			return $DataPromise(promise);
		}
		return DetectWinFactory;
	})
	.factory('$MakeMove', function(CurrentGame) {
		var MakeMoveFactory = function MakeMoveFactory () {}
		return MakeMoveFactory;
	});