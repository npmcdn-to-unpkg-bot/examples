var module = angular.module('my');

Implementation.$inject = ["$scope", "$q"];
function Implementation($scope, $q) {
	var that = this;
	
	that.$onChanges = function() {
		$scope.save = that.save ? that.save : "Save";
		$scope.cancel = that.cancel ? that.cancel : "Cancel";
	};

	that.hasErrors = function() {
		if (!(
			(that.myForm) &&
			(that.myForm.submitErrors) &&
			(that.myForm.submitErrors.errors) &&
			(that.myForm.submitErrors.errors.length === 0) &&
			(that.myForm.submitErrors.fieldErrors)
			)) {
			return false;
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
	ClearErrors();
	
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
		return that.myForm && (!that.myForm.$invalid) && (!that.myForm.$pristine) && (!that.hasErrors());
	};
	
	that.onBtnSave = function() {
		ClearErrors();
		if ((typeof that.onSave === "function") &&
			that.isBtnSaveEnabled()) {
			try {
				var r = that.onSave(addError);
				$q.when(r).catch(function(e) {
					addError("Operation failed " + e);
				});
			} catch (e) {
				addError("Operation failed " + e);
			}
		}
	};

	that.onBtnCancel = function() {
		if (typeof that.onCancel === "function") {
			that.onCancel();
		}
	};
}

module.component('myForm', {
	templateUrl: 'my/myForm.html',
	transclude: true,
	bindings: {
		save: "@",
		onSave: "=",
		cancel: "@",
		onCancel: "="
	},
	controller: Implementation
});
