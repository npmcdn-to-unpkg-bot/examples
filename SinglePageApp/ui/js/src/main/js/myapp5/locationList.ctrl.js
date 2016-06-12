var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$resource", "$routeParams", "$timeout", "locationService", "slavi-logger", "slavi-utils"];
function Implementation($scope, $resource, $routeParams, $timeout, service, logger, utils) {
	var that = this;
	that.service = service;
	$scope.service = service;
	that.query = "";
	that.delayedRunner = utils.delayedRunner();
	
	that.isDone = function() {
		return that.delayedRunner.isDone();
	};
	
	this.onChange = function() {
		that.delayedRunner.run(function() {
			service.selectedItem = null;
			service.loadData(that.query);
		});
	};

	that.onSelect = function(item) {
		// toggle selected item
		service.selectedItem = item === service.selectedItem ? null : item;
	};
}

module.component("locationList", {
	templateUrl: "myapp5/locationList.html",
	controller: Implementation
});
