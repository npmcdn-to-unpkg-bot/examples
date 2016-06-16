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
	$scope.$watchCollection("form[name].$error", function(newValue) {
		that.errors = [];
		var validators = that.form[that.name].$validators;
		var asyncValidators = that.form[that.name].$asyncValidators;
		angular.forEach(newValue, function(value, key) {
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
		that.hasErrors = that.errors.length !== 0;
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
