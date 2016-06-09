// TODO: Nice tips at: http://www.bennadel.com/blog/2616-aborting-ajax-requests-using-http-and-angularjs.htm
var module = angular.module("slavi-utils");

Implementation.$inject = ["$timeout", "$location", "slavi-logger"];
function Implementation($timeout, $location, logger) {
	var that = this;
	that.delayedRunner = DelayedRunner;

	function DelayedRunner() {
		var result = {
			delay: 700, // milliseconds
			promise: null,
			run: run,
			isDone: isDone
		};

		function isDone() {
			return (result.promise === null) || result.promise.isDone();
		}
		
		function run(fn) {
			if (result.promise !== null) {
				$timeout.cancel(result.promise);
			}
			result.promise = $timeout(fn, result.delay);
		}
		
		return result;
	}
	
	that.parentPath = function(path) {
		if (!path) {
			path = $location.path();
		}
		var lastIndex = path.length - (path.endsWith("/") ? 1 : 0);
		return path.slice(0, path.lastIndexOf("/", lastIndex));
	};
	
	that.navigateToParent = function() {
		$location.path(that.parentPath());
	};
	
	that.log = logger.log;
}
module.service("slavi-utils", Implementation);
