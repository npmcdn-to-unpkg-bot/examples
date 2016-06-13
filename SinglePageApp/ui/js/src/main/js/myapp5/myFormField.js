var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "slavi-logger"];
function Implementation($scope, logger) {
	var that = this;
	that.$onInit = function() {
		logger.log("myPanel ", that.myPanel);
		logger.log("form ", that.form);
	};

	that.hasErrors = function() {
		return (
			(that.form) && 
			(that.form.$error) && 
			(that.form.$error[that.name]));
	};

	that.hasErrors2 = function() {
		if (that.form) {
			if (that.form.$error) {
				
			} else {
				logger.log("$error is null");
			}
		} else {
			logger.log("form is null");
		}
			
		return (
			(that.form) && 
			(that.form.$error) && 
			(that.form.$error[that.name]));
	};
}

module.component('myFormField', {
	templateUrl: 'myapp5/myFormField.html',
	transclude: true,
	scope: false,
	require: { 
		form: "^form",
		myPanel: "^myPanel"
	},
	bindings: {
		label: "<",
		name: "<"
	},
	controller: Implementation,
});
