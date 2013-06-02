
describe('tictac-models', function(){
	beforeEach(function() {
		module('tictac-models');
	});
	it('has player', function() {
		inject(function(Player) {
			expect(Player).any;
		});
	});
	it('has board', function() {
		inject(function(Board) {
			expect(Board).any;
		});
	});
	it('has game', function() {
		inject(function(Game){
			expect(Game).any;
		});
	});
});