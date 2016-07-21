/**
 * Registers $scope listeners: "myFormClearErrors" and "myFormAddError".
 * The "myFormAddError" accepts two parameters: error object and name of the error.
 * If the name of the error is "submitError" then the error object is expected to be the
 * error object passed by $resource to the $error method of the promise
 */

var module = angular.module('my');

Implementation.$inject = ["$scope", "$q"];
function Implementation($scope, $q) {
	var that = this;

	that.$onChanges = function() {
		$scope.save = that.save ? that.save : "Save";
		$scope.cancel = that.cancel ? that.cancel : "Cancel";
		var lw;
		if (that.labelWidth >= 1 && that.labelWidth <= 11) {
			lw = that.labelWidth;
		} else if (that.labelWidth > 11) {
			lw = 11;
		} else if (that.labelWidth < 1) {
			lw = 1;
		} else {
			lw = 2;
		}
		$scope.btnGroupClass = "col-sm-offset-" + lw + " col-sm-" + (12-lw);

		ClearErrors();
	};

	that.hasErrors = function() {
		if (!(
			(that.myForm) &&
			(that.myForm.submitErrors) &&
			(that.myForm.submitErrors.errors) &&
			(that.myForm.submitErrors.fieldErrors)
			)) {
			return false;
		}
		if (that.myForm.submitErrors.errors.length !== 0) {
			return true;
		}
		for (var i in that.myForm.submitErrors.fieldErrors) {
			var e = that.myForm.submitErrors.fieldErrors[i];
			if (!(
					(e) &&
					(e.length === 0)
				)) {
				return true;
			}
		}
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
	
	$scope.$on("myFormClearErrors", ClearErrors);
	$scope.$on("myFormAddError", function(event, error, name) {
		addError(error, name);
	});
	
	function addError(error, name) {
		if (!error)
			return;
		if (name === "submitError") {
			if (error.status === 418) {
				// "I'm a tea pod." Server validation error.
				angular.merge(that.myForm.submitErrors, error.data);
			} else {
				addError("Operation failed with http error code " + error.status + " " + error.statusText);
			}
		} else if (name) {
			var e = that.myForm.submitErrors.fieldErrors[name];
			if (!Array.isArray(e)) {
				e = [];
				that.myForm.submitErrors.fieldErrors[name] = e;
			}
			e.push(error);
		} else {
			that.myForm.submitErrors.errors.push(error);
		}
	}
	
	that.isBtnSaveEnabled = function() {
		return that.myForm && (!that.myForm.$invalid) && (!that.myForm.$pristine);
	};
	
	that.onBtnSave = function() {
		ClearErrors();
		if (that.isBtnSaveEnabled()) {
			that.onSave();
		}
	};

	that.onBtnCancel = function() {
		that.onCancel();
	};
}

module.component('myForm', {
	templateUrl: 'my/myForm.html',
	transclude: true,
	bindings: {
		labelWidth: "@",
		save: "@",
		onSave: "&",
		cancel: "@",
		onCancel: "&"
	},
	controller: Implementation
});
