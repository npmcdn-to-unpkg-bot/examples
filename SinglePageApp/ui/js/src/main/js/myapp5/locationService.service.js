var module = angular.module('myapp5');

Implementation.$inject = ["$q", "$resource", "slavi-logger"];
function Implementation($q, $resource, logger) {
	var that = this;

	that.resource = $resource("http://localhost:8080/examples.spa.backend/api/location/:id", {
		callback: "JSON_CALLBACK",
		format: "json",
		search: "",
		page: 0,
		size: 10,
		order: "name|desc"
	}, {
		search: { method: "jsonp" },
		delete: { method: "delete", params: { id: "@id" }},
		load: { method: "get", params: { id: "@id" }},
		new: { method: "get", params: { id: "new" }},
		save: { method: "post" }
	}, {
		stripTrailingSlashes: false,
		cancellable: true
	});
	
	that.queryData = function(search) {
		return that.resource.search({ search: search });
	};
}

module.service('locationService', Implementation);
