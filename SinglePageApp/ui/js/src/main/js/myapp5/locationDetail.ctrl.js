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
		<div class="panel panel-default"> \
			<div class="panel-heading">Details</div>\
			<div class="panel-body"> \
				<div class="form-horizontal"> \
					<div class="form-group">\
						<label class="control-label col-sm-2">ID:</label>\
						<div class="col-sm-10"><p class="form-control-static">{{item.id}}</p></div> \
					</div>\
					<div class="form-group">\
						<label class="control-label col-sm-2">Name:</label>\
						<div class="col-sm-10"><input type="text" class="form-control" ng-model="item.name" /></div> \
					</div>\
					<div class="form-group"> \
						<div class="col-sm-offset-2 col-sm-10"><button class="btn btn-primary" ng-click="$ctrl.onDone()">Done</button></div> \
					</div>\
				</div> \
			</div> \
		</div>',
	bindings: {
		item: "<",
		save: "&"
	},
	controller: Implementation
});
