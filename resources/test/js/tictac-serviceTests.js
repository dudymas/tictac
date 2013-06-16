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
			expect(ComputerMove).toBeDefined();
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
			expect(DetectWin).toBeDefined();
		});
	});
	describe('DetectWin', function() {
		var detectWin, game;
		beforeEach(inject(function(CurrentGame, DetectWin) {
			detectWin = DetectWin;
			game = CurrentGame;
		}));

		it('should be a function', function () {
			expect(typeof(detectWin)).toBe('function');
		});
		it('should POST to /detect-win', function () {
			$httpBackend.expectPOST('/detect-win').respond(null);
			detectWin();
			$httpBackend.flush();
		});
		it('should send the CurrentGame', function () {
			$httpBackend.expectPOST('/detect-win', game).respond(null);
			detectWin();
			$httpBackend.flush();
		});
		it('should return the result', function () {
			var result = {some:"data"};
			$httpBackend.expectPOST('/detect-win', game).respond(result);
			detectWin().then(function(d) {expect(d).toBe(result)});
			$httpBackend.flush();
		});
	});
	afterEach(function() {
	  $httpBackend.verifyNoOutstandingExpectation();
	  $httpBackend.verifyNoOutstandingRequest();
	});
});