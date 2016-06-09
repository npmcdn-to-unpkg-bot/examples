var module = angular.module('myapp5', ["ngRoute", "ngResource", "slavi-log", "slavi-utils"]);

module.value('$routerRootComponent', 'myapp');

Implementation.$inject = ["$provide", "$routeProvider", "$locationProvider"];
function Implementation($provide, $routeProvider, $locationProvider) {
	/**
	 * Add a state property and a isDone() function to all deferred objects.
	 * Code borrowed from http://stackoverflow.com/questions/24091513/get-state-of-angular-deferred/24091953#24091953
	 */
	$provide.decorator('$q', ["$delegate", function ($delegate) {
		var defer = $delegate.defer;
		$delegate.defer = function() {
			var deferred = defer();
			deferred.promise.state = deferred.state = 0; // pending
			deferred.promise.then(function() {
				deferred.promise.state = deferred.state = 1; // resolved
			}, function () {
				deferred.promise.state = deferred.state = 2; // rejected
			});
			
			deferred.isDone = deferred.promise.isDone = function() {
				return deferred.state !== 0;
			};
			
			return deferred;
		};
		return $delegate;
	}]);

	$locationProvider.html5Mode(false);
	$locationProvider.hashPrefix('!');
	
	$routeProvider
	.when("/search",		{ template: "<search-list></search-list>" })
	.when("/link1",			{ template: "<link1-list></link1-list>" })
	.when("/link1/:id",		{ template: "<link1-detail></link1-detail>" })
	.when("/link2", 		{ template: "<link2></link2>" })
	.when("/location",		{ template: "<location-list></location-list>" });

}
module.config(Implementation);