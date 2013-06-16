
describe('tictac-models', function(){
	beforeEach(function() {
		module('tictac-models');
	});

	it('has player', function() {
		inject(function(Player) {
			expect(Player).toBeDefined();
		});
	});

	describe('player', function () {
		var player;
		var piece = "O";
		var type = "test";

		beforeEach(function() {
			inject(function(Player) {
				player = Player.create(piece, type);
			});
		});
		
		it('should have a piece', function () {
			expect(player["game-piece"]).toBe(piece);
		});
		it('should have a type', function () {
			expect(player.type).toBe(type);
		});
	});
	it('has board', function() {
		inject(function(Board) {
			expect(Board).toBeDefined();
		});
	});
	describe('board', function () {
		var board;
		var rowCount = 3;
		var rowLength = 3;

		beforeEach(function(){
			inject(function(Board) {
				board = Board.create(rowCount, rowLength);
			});
		});

		it('should be an array', function () {
			expect(board.length).toBeDefined();
		});
		it('should contain the specified number of rows', function () {
			expect(board.length).toBe(rowCount);
		});
		it('should have rows with the specified length', function () {
			while(board.length)
				expect(board.pop().length).toBe(rowLength);
		});
	});
	it('has game', function() {
		inject(function(Game){
			expect(Game).toBeDefined();
		});
	});
	describe('Game', function () {
		var game, players, board;
		beforeEach(function() {
			inject(function(Game){
				players = [{}, {}];
				board = [ [] ];
				game = Game.create(players, board);
			});
		});
		it('should have players', function () {
			expect(game.players).toBeDefined();
		});
		it('should have the players that were specified', function () {
			while (game.players.length)
				expect(players).toContain(game.players.pop());
		});
		it('should have a board', function () {
			expect(game.board).toBeDefined();
		});
		it('should contain the board that was specified', function () {
			expect(game.board).toBe(board);
		});
	});
});