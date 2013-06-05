describe('tictac-services', function () {
	var $httpBackend;
	beforeEach(function() {
		module('tictac-services');
		inject(function(_$httpBackend_) {
			$httpBackend = _$httpBackend_;
		} );
	});

	it('has computer move service', function () {
		inject(function(ComputerMove){
			expect(ComputerMove).any;
		});
	});
	describe('ComputerMove', function() {
		var computerMove, game;
		beforeEach(inject(function(CurrentGame, ComputerMove) {
			computerMove = ComputerMove;
			game = CurrentGame;
		}));

		it('is a function', function() {
			expect(typeof(computerMove)).toBe("function");
		});
		it('POSTs to /move', function() {
			$httpBackend.expectPOST('/move').respond(200, null);
			computerMove();
			$httpBackend.flush();
		});
		it('sends the CurrentGame', function() {
			$httpBackend.expectPOST('/move', game).respond(200, null);
			computerMove();
			$httpBackend.flush();
		});
		it('returns the move it recieved', function() {
			var mooove = "moooove it";
			$httpBackend.expectPOST('/move', game).respond(200, mooove);
			computerMove().then(function(result) {expect(result).toBe(mooove);});
			$httpBackend.flush();
		});
	});
	it('has win detection service', function () {
		inject(function(DetectWin){
			expect(DetectWin).any;
		});
	});
	describe('ComputerMove', function() {

	});
	afterEach(function() {
	  $httpBackend.verifyNoOutstandingExpectation();
	  $httpBackend.verifyNoOutstandingRequest();
	});
});