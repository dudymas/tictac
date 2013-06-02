describe('core', function () {
	beforeEach(function() {
		module('tictac-core');
	});

	it('has Player1', function () {
		inject(function(Player1){
			expect(Player1).any;
		});
	});
	it('has Player2', function () {
		inject(function(Player2){
			expect(Player2).any;
		});
	});
	it('has Board', function () {
		inject(function(Board){
			expect(Board).any;
		});
	});
	it('has Game', function () {
		inject(function(Game){
			expect(Game).any;
		});
	});
});