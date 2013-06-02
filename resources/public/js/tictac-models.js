angular.module('tictac-models',[])
	.factory("Player", function() {
		var Player = function Player () {};
		return Player;
	})
	.factory("Board", function() {
		var Board = function Board () {};
		return Board;
	})
	.factory("Game", function() {
		var Game = function Game (players, board) {};
		return Game;
	});