describe('tictac-services', function () {
	var $httpBackend, game;
	beforeEach(function() {
		game = {test : 'game'};
		module('tictac-services', function($provide) {
			game.spy = jasmine.createSpy('"CurrentGame factory"');
			var gameSpy = function() {
				game.spy();
				return game;
			};
			$provide.factory('CurrentGame', gameSpy)
		});
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
		var computerMove;
		beforeEach(inject(function(ComputerMove) {
			computerMove = ComputerMove;
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
		var detectWin;
		beforeEach(inject(function(DetectWin) {
			detectWin = DetectWin;
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

	it('has move making service', function () {
		inject(function($MakeMove) {
			expect($MakeMove).toBeDefined();
		});
	});
	describe('$MakeMove', function () {
		var $makeMove;

		beforeEach(function() {
			inject(function($MakeMove) {
				$makeMove = $MakeMove;
			});
		});

		it('should use CurrentGame', function () {
			expect(game.spy).toHaveBeenCalled();
		});
		it('should try to update the board', function () {});
		it('should not allow moves out of turn', function () {});
		it('should give computer players a turn', function () {});
	});
	afterEach(function() {
	  $httpBackend.verifyNoOutstandingExpectation();
	  $httpBackend.verifyNoOutstandingRequest();
	});
});