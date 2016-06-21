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

	that.hasErrors = function() {
		if (!(
			(that.myForm) &&
			(that.myForm.submitErrors) &&
			(that.myForm.submitErrors.errors) &&
			(that.myForm.submitErrors.errors.length === 0) &&
			(that.myForm.submitErrors.fieldErrors)
			)) {
			logger.log("hasErrors 1 ", that.myForm.submitErrors);
			return false;
		}
		for (var i in that.myForm.submitErrors.fieldErrors) {
			var e = that.myForm.submitErrors.fieldErrors[i];
			if (!(
					(e) &&
					(e.length === 0)
				)) {
				logger.log("hasErrors 2 ", e);
				return true;
			}
		}
		logger.log("hasErrors 3 ", that.myForm.submitErrors);
		return false;
	};
	
	function ClearErrors() {
		if (!that.myForm)
			that.myForm = {};
		if (!that.myForm.submitErrors) {
			that.myForm.submitErrors = {
				errors: [],
				fieldErrors: {}
			};
		}
		that.myForm.submitErrors.errors.length = 0;
		for (var i in that.myForm.submitErrors.fieldErrors) {
			that.myForm.submitErrors.fieldErrors[i].length = 0;
		}
	}
	ClearErrors();
	
	function Validate() {
		if ($scope.item.name.startsWith("w")) {
			that.myForm.submitErrors.push("Name should not start with w.");
		}
	}
	
	that.onDone = function() {
		ClearErrors();
		Validate();
		if (that.myForm && (!that.myForm.$invalid) && (!that.myForm.$pristine) && (!that.hasErrors())) {
			service.updateSelected($scope.item)
			.then(function(d) {
				logger.log("Saved ", d);
				service.selectedItem = null;
			}, function(e) {
				if (e.status === 418) {
					// "I'm a tea pod." Server validation error.
					angular.merge(that.myForm.submitErrors, e.data);
				} else {
					that.myForm.submitErrors.push("Operation failed with http error code " + e.status + " " + e.statusText);
				}
			});
		} else {
			console.log("onDone ", that.hasErrors(), that.myForm);
		}
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
