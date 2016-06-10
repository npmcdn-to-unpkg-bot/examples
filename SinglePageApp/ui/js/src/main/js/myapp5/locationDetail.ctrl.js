var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "locationService", "slavi-logger"];
function Implementation($scope, locationService, logger) {
	var that = this;
	$scope.$watch("$ctrl.item", function(newValue) {
		$scope.item = angular.copy(newValue);
	});
	
	that.onDone = function() {
		var save = locationService.resource.save({}, $scope.item);
		save.$promise.then(function(d) {
			logger.log("Saved ", d);
			angular.merge($scope.$ctrl.item, $scope.item);
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
