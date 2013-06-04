describe('tictac-core', function () {
	beforeEach(function() {
		module('tictac-core');
	});

	it('has Player1', function () {
		inject(function(Player1){
			expect(Player1).any;
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
				//expect(Game.players[0]).toBe(player);
			});
		});
	});
	it('has Player2', function () {
		inject(function(Player2){
			expect(Player2).any;
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
	});
	it('has CurrentBoard', function () {
		inject(function(CurrentBoard){
			expect(CurrentBoard).any;
		});
	});
	it('has CurrentGame', function () {
		inject(function(CurrentGame){
			expect(CurrentGame).any;
		});
	});
});