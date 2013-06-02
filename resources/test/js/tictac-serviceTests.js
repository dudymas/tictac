describe('tictac-services', function () {
	beforeEach(function() {
		module('tictac-services');
	});

	it('has computer move service', function () {
		inject(function(ComputerMove){
			expect(ComputerMove).any;
		});
	});
	it('has win detection service', function () {
		inject(function(DetectWin){
			expect(DetectWin).any;
		});
	});
});