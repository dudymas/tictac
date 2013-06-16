describe('tictac-controllers', function () {
	var game, moveSpy;
	beforeEach(function() {
		game = {board : [[null]], turn : {player: null}};
		game.reset = jasmine.createSpy("'game reset'");
		module('tictac-controllers', function($provide) {
			var gameSpy = function() {
				return game;
			};
			$provide.factory('CurrentGame', gameSpy);
			moveSpy = jasmine.createSpy('"$MakeMove"');
			$provide.factory('$MakeMove', function() {return moveSpy});
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
		var ctrl, scope;

		beforeEach(function() {
			inject(function($controller) {
				scope = {};
				ctrl = $controller('gameCtrl', {$scope: scope});
			});
		});

		it('should add reset() to the scope', function () {
			expect(scope.reset).toBeDefined();
			expect(typeof(scope.reset)).toBe('function');
		});
		it('should call reset on game', function () {
			scope.reset();
			expect(game.reset).toHaveBeenCalled();
		});
	});

	it('has rowCtrl', function () {
		inject(function($controller) {
			var rowCtrl = $controller('rowCtrl', {$scope : {}});
			expect(rowCtrl).toBeDefined();
		})
	});
	describe('rowCtrl', function () {
		var ctrl, scope;

		beforeEach(function() {
			inject(function($controller) {
				scope = {$index: 9001 };//over nine-thousand!!!!
				ctrl = $controller('rowCtrl', {$scope: scope});
			});
		});

		it('should define setPiece() on scope', function () {
			expect(scope.setPiece).toBeDefined();
			expect(typeof(scope.setPiece)).toBe('function');
		});
		describe('scope.setPiece', function () {
			beforeEach(function () {
				game.turn.player = "current player";
				scope.setPiece(1);
			});

			it('should call $MakeMove', function () {
				expect(moveSpy).toHaveBeenCalled();
			});
			it('should pass player who is in CurrentGame.turn', function () {
				expect(moveSpy.mostRecentCall.args[0]).toBe(game.turn.player);
			});
			it('should pass $index and position to $MakeMove', function () {
				expect(moveSpy.mostRecentCall.args[1][0]).toBe(scope.$index);
				expect(moveSpy.mostRecentCall.args[1][1]).toBe(1);
			});
		});
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