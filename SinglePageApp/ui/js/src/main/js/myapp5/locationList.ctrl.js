var module = angular.module('myapp5');

Implementation.$inject = ["$resource", "$routeParams", "$timeout", "locationService", "slavi-logger", "slavi-utils"];
function Implementation($resource, $routeParams, $timeout, locationService, logger, utils) {
	var that = this;
	that.query = "";
	that.selectedItem = null;
	that.data = locationService.queryData();
	that.delayedRunner = utils.delayedRunner();
	
	that.isDone = function() {
		return that.delayedRunner.isDone();
	};
	
	that.isSelected = function(item) {
		return (item !== null) && (item === that.selectedItem);
	};
	
	this.onChange = function() {
		that.delayedRunner.run(function() {
			that.selectedItem = null;
			that.data = locationService.queryData(that.query);
		});
	};

	that.onSelect = function(item) {
		// toggle selected item
		that.selectedItem = item === that.selectedItem ? null : item;
	};
}

module.component("locationList", {
	templateUrl: "myapp5/locationList.html",
	controller: Implementation
});
