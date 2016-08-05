var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "slavi-logger", "$timeout"];
function Implementation($scope, logger, $timeout) {
	var that = this;

	that.$onChanges = function() {
		logger.log("onChange");
	};

	$scope.$watch("$ctrl.item", function(newValue) {
		$scope.item = angular.copy(newValue);
	});

	that.onCancel = function() {
		console.log("onCancel");
	};
	
	that.onSave = function() {
		console.log("onSave");
	};
}

module.component('myFormGen', {
	templateUrl: 'myapp5/myFormGen.html',
	bindings: {
		item: "=",
		formMeta: "="
//		save: "&"
	},
	controller: Implementation
});
