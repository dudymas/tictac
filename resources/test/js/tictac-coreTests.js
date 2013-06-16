describe('tictac-core', function () {
	beforeEach(function() {
		module('tictac-core');
	});

	it('has Player1', function () {
		inject(function(Player1){
			expect(Player1).toBeDefined();
		});
	});
	describe('Player1', function(){
		var player;
		beforeEach(inject(function(Player1) {
			player = Player1;
		}));

		it('should be a Player',function() {
			expect(player.type).toBeDefined();
			expect(player["game-piece"]).toBeDefined();
		});
		it('should be the first player', function() {
			inject(function(CurrentGame) {
				expect(CurrentGame.players).toBeDefined();
				expect(CurrentGame.players[0]).toBe(player);
			});
		});
		it('should default to a human player', function () {
			expect(player.type).toBeDefined();
			expect(player.type).toBe('human');
		});
		it('should default to be O', function () {
			expect(player["game-piece"]).toBe('O');
		});
	});
	it('has Player2', function () {
		inject(function(Player2){
			expect(Player2).toBeDefined();
		});
	});
	describe('Player2', function(){
		var player;
		beforeEach(inject(function(Player2) {
			player = Player2;
		}));

		it('should be a Player',function() {
			expect(player.type).toBeDefined();
			expect(player["game-piece"]).toBeDefined();
		});
		it('should be the second player', function() {
			inject(function(CurrentGame) {
				expect(CurrentGame.players).toBeDefined();
				expect(CurrentGame.players[1]).toBe(player);
			});
		});
		it('should default to a computer player', function () {
			expect(player.type).toBeDefined();
			expect(player.type).toBe('computer');
		});
		it('should default to be X', function () {
			expect(player["game-piece"]).toBe('X');
		});
	});
	it('has CurrentBoard in 3x3 configuration by default', function () {
		inject(function(CurrentBoard){
			expect(CurrentBoard).toBeDefined();
			expect(CurrentBoard.length).toBe(3);
			expect(CurrentBoard[0].length).toBe(3);
		});
	});
	it('has CurrentGame', function () {
		inject(function(CurrentGame){
			expect(CurrentGame).toBeDefined();
		});
	});
	describe('CurrentGame', function() {
		var game, board;
		beforeEach(function() {
			inject(function(CurrentBoard, CurrentGame) {
				game = CurrentGame;
				board = CurrentBoard;
			});
		})

		it('has the current CurrentBoard', function() {
			expect(game.board).toBe(board);
		});
		it('has the current players', function() {
			inject(function(Player1, Player2) {
				while(game.players.length > 0)
						expect([Player1, Player2]).toContain(game.players.pop());
			});
		});
		it('has initialized the first turn', function () {
			expect(game.turn).toBeDefined();
		});
		it('has made player1 the first player to take a turn', function () {
			inject(function(Player1) {
				expect(game.turn.player).toBe(Player1);
			});
		});
	});
});