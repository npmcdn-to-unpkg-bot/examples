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
	
	// that.types = [ "Unknown", "Headquarter", "Office", "Home" ];
	
	that.formMeta = {
		fields: [
			{
				label: "ID:",
				name: "id",
				type: "label"
			},
			{
				label: "Name:",
				name: "name",
				type: "text",
				minLength: 3,
				maxLength: 15,
				required: true,
				trim: true,
				pattern: "[A-Za-z@0-9]+"
			},
			{
				label: "e-mail:",
				name: "email",
				type: "email"
			},
			{
				label: "type:",
				name: "type",
				type: "radio",
				values: [ "Unknown", "Headquarter", "Office", "Home" ]
			},
			{
				label: "type (same with combo):",
				name: "type",
				type: "combo",
				values: [ "Unknown", "Headquarter", "Office", "Home" ]
			}
		]
	};
}

module.component('myFormGen', {
	templateUrl: 'myapp5/myFormGen.html',
	bindings: {
		item: "="
//		formMeta: "="
//		save: "&"
	},
	controller: Implementation
});
