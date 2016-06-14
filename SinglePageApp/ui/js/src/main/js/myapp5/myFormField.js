var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$element", "$attrs", "slavi-logger"];
function Implementation($scope, $element, $attrs, logger) {
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
				var m;
				if (v && v.message) {
					m = v.message;
				} else {
					m = "Validator " + key + " failed.";
				}
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
		label: "<",
		name: "<"
	},
	controller: Implementation,
});


