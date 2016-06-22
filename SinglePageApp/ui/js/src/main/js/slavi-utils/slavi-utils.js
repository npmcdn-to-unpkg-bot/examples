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

	that.calcPath = function(relativePath, basePath) {
		var path = basePath ? String(basePath) : $location.path();
		if (!path.endsWith("/")) {
			path += "/";
		}
		var segments = relativePath.split("/");
		for (var i = 0; i < segments.length; i++) {
			var p = segments[i];
			if (p === "" || p === ".")
				continue;
			if (p === "..") {
				var lastIndex = path.lastIndexOf("/", path.length - 2);
				if (lastIndex > 0) {
					path = path.slice(0, lastIndex + 1);
				}
				continue;
			}
			path += p + "/";
			
		}
		return path.slice(0, -1);
	};
	
	that.navigateTo = function(relativePath) {
		$location.path(that.calcPath(relativePath));
	};
	
	that.log = logger.log;
}
module.service("slavi-utils", Implementation);
