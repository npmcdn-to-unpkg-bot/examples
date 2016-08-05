var module = angular.module('myapp5');

Factory.$inject = ["slavi-logger", "$timeout"];
function Factory(logger, $timeout) {
	
	Instance.$inject = ["$scope"];
	function Instance($scope) {
		var that = this;
	
		console.log("my-form-field-gen created");
		
		that.$onChanges = function() {
			logger.log("onChange");
		};
	/*
		$scope.$watch("$ctrl.item", function(newValue) {
			$scope.item = angular.copy(newValue);
		});
	*/
		that.onCancel = function() {
			console.log("onCancel");
		};
		
		that.onSave = function() {
			console.log("onSave");
		};
	}
	
	getTemplate.$inject = ["$element", "$attrs"];
	function getTemplate($element, $attrs) {
		console.log($element);
		//var meta = $scope.$eval($attrs[formFieldMeta]);
		console.log($attrs);
		return "<pre>WTF?</pre>";
	}

	// link.$inject = ["$scope", "$element", "$attrs"];
	function link($scope, $element, $attrs, ctrl, $transclude) {
		// ctrl.template
		console.log("inside link");
		ctrl.template = "<pre>WTF?</pre>";
		var clone = $transclude($scope, function(clone) {
			console.log("some where inside transclude");
		});
	}

	function compile(element, attr) {
		console.log("inside compile");
		dd.template = "<p>waiting even more...</p>";
		return link;
	}
	
	var dd = {
//		template: "<p>waiting...</p>",
		transclude: true,
		restrict: 'E',
		scope: false,
		controller: Instance,
		compile: compile
	};
	return dd;
}

module.directive("myFormFieldGen", Factory);

/*
module.component('myFormFieldGen', {
	template: getTemplate,
	bindings: {
//		item: "=",
		formFieldMeta: "="
//		save: "&"
	},
	controller: Implementation
});
*/