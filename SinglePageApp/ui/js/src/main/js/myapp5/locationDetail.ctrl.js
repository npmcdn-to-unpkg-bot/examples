var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "locationService", "slavi-logger"];
function Implementation($scope, service, logger) {
	var that = this;

	that.$onChanges = function() {
		logger.log("onChange");
	};

	$scope.$watch("$ctrl.item", function(newValue) {
		$scope.item = angular.copy(newValue);
	});

	that.onDone = function() {
		logger.log("onDone()");
		service.updateSelected($scope.item)
		.then(function(d) {
			logger.log("Saved ", d);
			service.selectedItem = null;
		}, function(e) {
			logger.log("save failed ", e);
		});
	};
	
	that.hasErrors = function(form, attrName) {
		return form.$error[attrName];
	};
}

module.component('locationDetail', {
	templateUrl: 'myapp5/locationDetail.html',
	bindings: {
		item: "="
//		save: "&"
	},
	controller: Implementation
});
