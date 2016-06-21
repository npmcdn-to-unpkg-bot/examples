var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$q"];
function Implementation($scope, $q) {
	var that = this;
	
	that.$onChanges = function() {
		$scope.save = that.save ? that.save : "Save";
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
		console.log("addError", error, name);
		if (!error)
			return;
		if (name) {
			var e = that.myForm.submitErrors.fieldErrors[name];
			if (typeof e === undefined) {
				e = [];
				that.myForm.submitErrors.fieldErrors[name] = e;
			}
			e.push(error);
		} else {
			that.myForm.submitErrors.errors.push(error);
		}
	}
	
	that.isBtnSaveEnabled = function() {
		return true;
		// return that.myForm && (!that.myForm.$invalid) && (!that.myForm.$pristine) && (!that.hasErrors());
	};
	
	that.onBtnSave = function() {
		ClearErrors();
		if ((typeof that.onSave === "function") &&
			that.isBtnSaveEnabled()) {
			try {
				var r = that.onSave.call(that, addError);
				$q.when(r).catch(function(e) {
					console.log("Error 1 ", e);
					addError("Operation failed " + e);
				});
			} catch (e) {
				console.log("Error 2", e);
				addError("Operation failed " + e);
			}
		} else {
			console.log("onDone ", that.hasErrors(), that.myForm);
		}
	};
}

module.component('myForm', {
	templateUrl: 'myapp5/myForm.html',
	transclude: true,
	bindings: {
		save: "@",
		onSave: "="
	},
	controller: Implementation
});
