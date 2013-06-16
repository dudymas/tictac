describe('tictac-controllers', function () {
	var game;
	beforeEach(function() {
		game = {board : [[null]], turn : {player: null}};
		module('tictac-controllers', function($provide) {
			var gameSpy = function() {
				return game;
			};
			$provide.factory('CurrentGame', gameSpy);
		});
	});

	it('has boardCtrl', function () {
		inject(function($controller) {
			var boardCtrl = $controller('boardCtrl', {$scope : {}});
			expect(boardCtrl).toBeDefined();
		})
	});
	describe('boardCtrl', function () {
		var ctrl, scope;

		beforeEach(function() {
			inject(function($controller) {
				scope = {};
				ctrl = $controller('boardCtrl', {$scope: scope});
			});
		});

		it('should put board in scope', function () {
			expect(scope.board).toBe(game.board);
		});
		it('should put current turn in scope', function () {
			expect(scope.turn).toBe(game.turn);
		});
	});

	it('has gameCtrl', function () {
		inject(function($controller) {
			var gameCtrl = $controller('gameCtrl', {$scope : {}});
			expect(gameCtrl).toBeDefined();
		})
	});
	describe('gameCtrl', function () {
	});
	
	it('has rowCtrl', function () {
		inject(function($controller) {
			var rowCtrl = $controller('rowCtrl', {$scope : {}});
			expect(rowCtrl).toBeDefined();
		})
	});
	describe('rowCtrl', function () {
	});

	it('has winIndicatorCtrl', function () {
		inject(function($controller) {
			var winIndicatorCtrl = $controller('winIndicatorCtrl', {$scope : {}});
			expect(winIndicatorCtrl).toBeDefined();
		})
	});
	describe('winIndicatorCtrl', function () {
	});
});