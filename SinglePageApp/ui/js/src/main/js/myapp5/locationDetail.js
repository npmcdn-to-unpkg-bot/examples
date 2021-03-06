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

	that.onCancel = function() {
		console.log("onCancel");
		service.selectedItem = null;
	};
	
	that.onSave = function() {
		console.log("onSave");
		$scope.$broadcast("myFormAddError", "Just a dummy error");
		
		if ($scope.item.name.startsWith("w")) {
			$scope.$broadcast("myFormAddError", "Name should not start with w.");
			return;
		}

		return service.updateSelected($scope.item)
		.then(function(d) {
			logger.log("Saved ", d);
			service.selectedItem = null;
		}, function(e) {
			$scope.$broadcast("myFormAddError", e, "submitError");
		});
	};
	
	that.sameValueMessage = "Value is not the same";
	
	that.asyncValidate = function(value) {
		return $timeout(function() {
			return !String(value).endsWith("aa") ? "" : 
				"ASYNC: Must not end in double a, i.e. aa, but the value is " + value;
		}, 1000);
	};
	
	that.validate = function(value) {
		return !String(value).endsWith("a") ? "" : 
			"Must not end in a but the value is " + value;
	};

	that.types = [ "Unknown", "Headquarter", "Office", "Home" ];
}

module.component('locationDetail', {
	templateUrl: 'myapp5/locationDetail.html',
	bindings: {
		item: "="
//		save: "&"
	},
	controller: Implementation
});
