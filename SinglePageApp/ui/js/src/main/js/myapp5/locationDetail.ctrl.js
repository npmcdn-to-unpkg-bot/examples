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
	template: '\
		<h3>Details</h3>\
		<div class="form-horizontal"> \
			<div class="form-group">\
				<label class="control-label col-sm-1">ID:</label>\
				<input type="text" class="form-control col-sm-7" ng-model="item.id" /> \
			</div>\
			<div class="form-group">\
				<label class="control-label col-sm-1">Name:</label>\
				<input type="text" class="form-control col-sm-7" ng-model="item.name" /> \
			</div>\
			<button class="btn btn-primary" ng-click="$ctrl.onDone()">Done</button>\
		</div>',
	bindings: {
		item: "<",
		save: "&"
	},
	controller: Implementation
});
