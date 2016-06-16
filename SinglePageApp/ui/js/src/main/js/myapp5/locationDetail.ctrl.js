var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "locationService", "slavi-logger", "$timeout"];
function Implementation($scope, service, logger, $timeout) {
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

	that.sameValueMessage = "Value no the same";
	
	that.asyncValidate = function(value) {
		return $timeout(function() {
			return String(value).endsWith("aa") ? "" : 
				"ASYNC: Must end in double a, i.e. aa, but the value is " + value;
		}, 1000);
	};
	
	that.validate = function(value) {
		return String(value).endsWith("a") ? "" : 
			"Must end in a but the value is " + value;
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
