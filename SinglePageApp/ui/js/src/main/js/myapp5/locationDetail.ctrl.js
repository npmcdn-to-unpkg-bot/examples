var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "locationService", "slavi-logger"];
function Implementation($scope, service, logger) {
	var that = this;
	$scope.service = service;

	$scope.$watch("service.selectedItem", function(newValue) {
		$scope.item = angular.copy(newValue);
	});
	
	that.onDone = function() {
		service.updateSelected($scope.item)
		.then(function(d) {
			logger.log("Saved ", d);
			service.selectedItem = null;
		}, function(e) {
			logger.log("save failed ", e);
		});
	};
}

module.component('locationDetail', {
	templateUrl: 'myapp5/locationDetail.html',
	bindings: {
		item: "<",
		save: "&"
	},
	controller: Implementation
});
