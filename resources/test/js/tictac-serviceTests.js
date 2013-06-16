describe('tictac-services', function () {
	var $httpBackend, game;
	beforeEach(function() {
		game = {test : 'game'};
		module('tictac-services', function($provide) {
			game.spy = jasmine.createSpy('"CurrentGame factory"');
			game.board = { update : jasmine.createSpy('"CurrentBoard update"') };
			var gameSpy = function() {
				game.spy();
				return game;
			};
			$provide.factory('CurrentGame', gameSpy);
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
			expect(typeof(DetectWin)).toBe('function');
		});
	});
	describe('DetectWin', function() {
		var detectWin, result;

		beforeEach(inject(function(DetectWin) {
			detectWin = DetectWin;
			result = {some:"data"};
			$httpBackend.expectPOST('/detect-win', game).respond(function() {
				return [200, result];
			});
		}));
		afterEach(function () {
			$httpBackend.flush();
		});

		it('should POST to /detect-win', function () {
			detectWin();
		});
		it('should send the CurrentGame', function () {
			detectWin();
		});
		it('should filter stringified null', function () {
			result = "null";
			detectWin().then(function(d) {expect(d).toBeNull();})
		});
		it('should return the result', function () {
			detectWin().then(function(d) {expect(d).toBe(result)});
		});
		it('should put result in game.win', function () {
			detectWin().then(function() {expect(game.win).toBe(result)});
		});
	});

	it('has move making service', function () {
		inject(function($MakeMove) {
			expect($MakeMove).toBeDefined();
		});
	});
	describe('$MakeMove', function () {
		var $makeMove, player, position, computer, computerMove;

		beforeEach(function() {
			inject(function($MakeMove, ComputerMove) {
				$makeMove = $MakeMove;
				computerMove = ComputerMove;
			});
			player = {"game-piece": '\m/'};
			computer = {type : 'computer'};
			game.turn = {player: player};
			position = [2,5, 3]; //yay, 3d!
			game.players = [player, computer];
		});

		it('should use CurrentGame', function () {
			expect(game.spy).toHaveBeenCalled();
		});
		it('should try to update the board', function () {
			$makeMove(player);
			expect(game.board.update).toHaveBeenCalled();
		});
		it('should update board with game-piece', function () {
			$makeMove(player);
			expect(game.board.update.mostRecentCall.args[0]).toBe(player["game-piece"]);
		});
		it('should pass the position of the move', function () {
			$makeMove(player, position);
			expect(game.board.update.mostRecentCall.args[1]).toBe(position);
		});
		it('should not allow moves out of turn', function () {
			var otherPlayer = {some: "otherperson"};
			game.turn = { player : otherPlayer};
			$makeMove(player);
			expect(game.board.update).not.toHaveBeenCalled();
			expect(game.turn.player).toBe(otherPlayer);
			expect(game['last-turn']).not.toBeDefined();
		});
		it('should give next player a turn', function () {
			$makeMove(player);
			expect(game.turn.player).toBe(computer);
			$makeMove(computer);
			expect(game.turn.player).toBe(player);
		});
		it('should update the last-turn of game', function () {
			var lastTurn = game.turn;
			var pos = [3,5];
			$makeMove(player, pos);
			expect(game["last-turn"]).toBe(lastTurn);
			expect(game["last-turn"].position).toBe(pos);
			expect(game.turn).not.toBe(lastTurn);
		});
		it('should not allow moves once game is won', function () {
			game.win = {"winning-row" : "somewhere"};
			$makeMove(player, [2,3]);
			expect(game.board.update).not.toHaveBeenCalled();
			expect(game.turn.player).toBe(player);
			expect(game['last-turn']).not.toBeDefined();
		});
	});
	afterEach(function() {
	  $httpBackend.verifyNoOutstandingExpectation();
	  $httpBackend.verifyNoOutstandingRequest();
	});
});