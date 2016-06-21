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

	that.onSave = function(addError) {
		console.log("onSave");
		addError("Just a dummy error");
		if(true)return;
		
		if ($scope.item.name.startsWith("w")) {
			addError("Name should not start with w.");
			return;
		}

		service.updateSelected($scope.item)
		.then(function(d) {
			logger.log("Saved ", d);
			service.selectedItem = null;
		}, function(e) {
			console.log("onError");
			if (e.status === 418) {
				// "I'm a tea pod." Server validation error.
				angular.forEach(e.data.errors, function(value) {
					addError(value);
				});
				angular.forEach(e.data.fieldErrors, function(fieldError, name) {
					angular.forEach(fieldError, function(value) {
						addError(value, name);
					});
				});
			} else {
				addError("Operation failed with http error code " + e.status + " " + e.statusText);
			}
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
}

module.component('locationDetail', {
	templateUrl: 'myapp5/locationDetail.html',
	bindings: {
		item: "="
//		save: "&"
	},
	controller: Implementation
});
