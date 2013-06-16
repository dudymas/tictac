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
			return $DataPromise(promise)
				.then(function(d) {
					CurrentGame.win = d;
					return d;
				});
		}
		return DetectWinFactory;
	})
	.factory('$MakeMove', function(CurrentGame, Turn) {
		var MakeMoveFactory = function MakeMoveFactory (player, pos) {
			if (CurrentGame.turn.player == player)
				CurrentGame.board.update(player["game-piece"], pos);
			else return;
			CurrentGame.turn.position = pos;
			CurrentGame["last-turn"] = CurrentGame.turn;
			CurrentGame.turn = Turn.create();
			CurrentGame.turn.player = 
				player == CurrentGame.players[0] ? CurrentGame.players[1] : CurrentGame.players[0];
		};
		return MakeMoveFactory;
	});