var module = angular.module('myapp4.crisis-center');

Service.$inject = ["$q", "slavi-logger"];
function Service($q, logger) {
	var crisesPromise = $q.when([
		{id: 1, name: 'Princess Held Captive'},
		{id: 2, name: 'Dragon Burning Cities'},
		{id: 3, name: 'Giant Asteroid Heading For Earth'},
		{id: 4, name: 'Release Deadline Looms'}
	]);
	
	this.getCrises = function() {
		return crisesPromise;
	};
	
	this.getCrisis = function(id) {
		return crisesPromise.then(function(crises) {
			for(var i=0; i<crises.length; i++) {
				if (crises[i].id == id)
					return crises[i];
			}
		});
	};
}

module.service('crisisService', Service);
