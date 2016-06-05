var module = angular.module('myapp5', ["ngComponentRouter", "ngResource", "slavi-log", "slavi-utils"]);

module.value('$routerRootComponent', 'myapp');

Implementation.$inject = ["$provide"];
function Implementation($provide) {
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
}
module.config(Implementation);