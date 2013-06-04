angular.module('tictac-core',['tictac-models'])
	.factory('Player1', function(Player) { return Player.create("O", "human"); } )
	.factory('Player2',function(Player) { return Player.create("O", "human"); } )
	.value('CurrentBoard',{})
	.factory('CurrentGame',function(Game, Player1, Player2) {return Game.create([Player1, Player2]); });