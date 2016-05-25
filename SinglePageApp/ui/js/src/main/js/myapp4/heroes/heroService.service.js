var module = angular.module('myapp4.heroes');

Service.$inject = ["$q", "slavi-logger"];
function Service($q, logger) {
	logger.log($q);
	logger.log("QQQQ");

	var heroesPromise = $q.when([
		{ id: 11, name: 'Mr. Nice' },
		{ id: 12, name: 'Narco' },
		{ id: 13, name: 'Bombasto' },
		{ id: 14, name: 'Celeritas' },
		{ id: 15, name: 'Magneta' },
		{ id: 16, name: 'RubberMan' }
	]);

	this.getHeroes = function() {
		return heroesPromise;
	};
	
	this.getHero = function(id) {
		return heroesPromise.then(function(heroes) {
			for(var i=0; i<heroes.length; i++) {
				if ( heroes[i].id == id) return heroes[i];
			}
		});
	};
}

module.service('heroService', Service);
