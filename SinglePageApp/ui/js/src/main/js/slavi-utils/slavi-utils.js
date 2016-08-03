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

	/**
	 * Make a list of key) and V (value) objects.
	 * The supplied obj can be in the form:
	 * obj = [1, 2, "v", 4];
	 * obj = [ 
	 * 		{ k: 1, v:"one" },
	 * 		{ key: 2, val: "two" },
	 * 		{ id: 3, value: "three" },
	 * 		{ name: "4", label: "four" }];
	 * obj = { 1: "one", "2": "two", 3: "three" };
	 */
	that.toKeyValueList = function(obj) {
		var r = [];
		var isArray = obj && Array === obj.constructor; 
		angular.forEach(obj, function(v, k) {
			if (isArray) {
				if (v instanceof Object) {
					k = 
						v.key ? v.key :
						v.k ? v.k :
						v.id ? v.id :
						v.name ? v.name :
						k;
					v = 
						v.value ? v.value :
						v.val ? v.val :
						v.v ? v.v :
						v.label ? v.label :
						v;
				} else {
					k = v;
				}
			}
			r.push({ value: v, key: k });
		});
		return r;
	};
	
	that.log = logger.log;
}
module.service("slavi-utils", Implementation);
