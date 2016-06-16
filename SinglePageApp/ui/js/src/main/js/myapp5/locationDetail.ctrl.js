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

	
	that.errors = [];
	that.hasErrors = that.errors.length !== 0;
	
	function Validate() {
		that.errors = [];
		if ($scope.item.name.startsWith("z")) {
			that.errors.push("Name should not start with z.");
		}
		logger.log("Validate 1 ", $scope.item.name);
		logger.log("Validate 2 ", that.errors);
		that.hasErrors = that.errors.length !== 0;
	}
	
	that.onDone = function() {
		Validate();
		if (that.myForm && that.myForm.$valid && that.myForm.$dirty) {
			logger.log("onDone() ");
		}
/*
		service.updateSelected($scope.item)
		.then(function(d) {
			logger.log("Saved ", d);
			service.selectedItem = null;
		}, function(e) {
			logger.log("save failed ", e);
		});
*/
	};

	that.sameValueMessage = "Value is not the same";
	
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
