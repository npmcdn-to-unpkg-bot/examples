var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "locationService", "slavi-logger"];
function Implementation($scope, service, logger) {
	var that = this;
	$scope.service = service;

	$scope.$watch("service.selectedItem", function(newValue) {
		$scope.item = angular.copy(newValue);
	});
	
	that.onDone = function() {
		var newValue = angular.copy($scope.item);
		var oldValue = service.selectedItem;
		var save = service.resource.save({}, newValue);
		save.$promise.then(function(d) {
			logger.log("Saved ", d);
			angular.merge(oldValue, newValue);
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
