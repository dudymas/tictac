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
		var computerMove;
		beforeEach(inject(function(ComputerMove) {
			computerMove = ComputerMove;
		}));

		it('is a function', function() {
			expect(typeof(computerMove)).toBe("function");
		});
		it('calls $http', function() {
			//computerMove();
		});
		it('sends the CurrentGame');
		it('returns the move it recieved');
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