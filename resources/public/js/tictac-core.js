angular.module('tictac-core',['tictac-models'])
	.factory('Player1', function(Player) { return Player.create("O", "human"); } )
	.factory('Player2',function(Player) { return Player.create("O", "human"); } )
	.factory('CurrentBoard',function(Board) { return Board.create(3,3);})
	.factory('CurrentGame',function(Game, Player1, Player2, CurrentBoard, Turn) {
		var result = Game.create([Player1, Player2], CurrentBoard);
		result.turn = Turn.create();
		result.turn.player = Player1;
		return result;
	});