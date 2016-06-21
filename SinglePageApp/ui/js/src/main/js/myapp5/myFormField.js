var module = angular.module('myapp5');

/**
 * Usage example:
 * <form novalidate ng-model-options="{ updateOn: 'default blur', debounce: { default: 700, blur: 0 } }">
 *     <my-form-field data-label="My field:" data-name="myFieldName">
 *         <input 
 *             type="text" 
 *             name="myFieldName"
 *             ng-model="item.name"
 *             ng-required="true"
 *         />
 *     </my-form-field>
 * </form>
 */

Implementation.$inject = ["$scope"];
function Implementation($scope) {
	var that = this;
	that.hasErrors = false;
	that.errors = [];

	that.$onInit = function() {
		$scope.form = that.form;
		$scope.name = that.name;
	};
	function buildErrors(fieldField$error, formFieldSubmitError) {
		that.errors = [];
		
		var validators;
		if (that.form && that.form[that.name]) {
			validators = that.form[that.name].$validators;
		}
		if (!validators)
			validators = {};

		var asyncValidators;
		if (that.form && that.form[that.name]) {
			asyncValidators = that.form[that.name].$asyncValidators;
		}
		if (!asyncValidators)
			asyncValidators = {};
		
		angular.forEach(fieldField$error, function(value, key) {
			switch (key) {
			case "minlength":
				that.errors.push("Value is too short");
				break;
			case "maxlength":
				that.errors.push("Value is too long");
				break;
			case "max":
				that.errors.push("Value is too big");
				break;
			case "min":
				that.errors.push("Value is too small");
				break;
			case "required":
				that.errors.push("Value is required");
				break;
			case "email":
				that.errors.push("Not a valid e-mail");
				break;
			case "pattern":
				that.errors.push("Does not match the format");
				break;
			default:
				var v = validators[key];
				if (!v)
					v = asyncValidators[key];
				var m;
				if (v && v.message) {
					m = v.message;
				}
				if (!m)
					m = "Validation " + key + " failed.";
				that.errors.push(m);
				break;
			}
		});
		
		if (formFieldSubmitError) {
			for (var i = 0; i < formFieldSubmitError.length; i++) {
				that.errors.push(formFieldSubmitError[i]);
			}
		}
		that.hasErrors = that.errors.length !== 0;
	} 
	
	$scope.$watchCollection("form[name].$error", function(newValue) {
		var formFieldSubmitError;
		if (that.form &&
			that.form.submitErrors &&
			that.form.submitErrors.fieldErrors &&
			that.form.submitErrors.fieldErrors[that.name]) {
			formFieldSubmitError = that.form.submitErrors.fieldErrors[that.name];
		}
		if (!formFieldSubmitError)
			formFieldSubmitError = {};
		buildErrors(newValue, formFieldSubmitError);
	});

	$scope.$watchCollection("form.submitErrors.fieldErrors[name]", function(newValue) {
		var fieldField$error;
		if (that.form &&
			that.form[that.name] &&
			that.form[that.name].$error) {
			fieldField$error = that.form[that.name].$error;
		}
		if (!fieldField$error)
			fieldField$error = {};
		buildErrors(fieldField$error, newValue);
	});
}

module.component('myFormField', {
	templateUrl: 'myapp5/myFormField.html',
	transclude: true,
	require: { 
		form: "^form"
	},
	bindings: {
		label: "@",
		name: "@"
	},
	controller: Implementation,
});
