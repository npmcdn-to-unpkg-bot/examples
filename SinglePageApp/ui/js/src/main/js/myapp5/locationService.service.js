var module = angular.module('myapp5');

Implementation.$inject = ["$q", "$resource", "slavi-logger"];
function Implementation($q, $resource, logger) {
	var that = this;

	that.resource = $resource("http://localhost:8080/examples.spa.backend/api/location", {
		callback: "JSON_CALLBACK",
		format: "json"
	}, {
		get: { method: "jsonp" }
	}, {
		stripTrailingSlashes: false,
		cancellable: true
	});
	
	that.queryData = function(queryStr) {
		return that.resource.get({ q: queryStr });
	};
}

module.service('locationService', Implementation);
