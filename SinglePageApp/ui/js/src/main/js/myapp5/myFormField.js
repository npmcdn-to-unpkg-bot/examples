var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "slavi-logger"];
function Implementation($scope, logger) {
	var that = this;
	that.hasErrors = false;
	that.errors = [];

	that.$onInit = function() {
		$scope.form = that.form;
		$scope.name = that.name;
		
		
	};
	$scope.$watch("form.$error[name]", function(newValue) {
		that.hasErrors = (newValue) ? true : false;
		
		that.errors = [];
/*		if (newValue.minlength)
			that.errors.push("Minimum length is ")
*/	});
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


