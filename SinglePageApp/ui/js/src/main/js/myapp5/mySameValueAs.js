var module = angular.module('myapp5');

Implementation.$inject = ["slavi-logger"];
function Implementation(logger) {
	return {
		restrict: 'A',
		require: 'ngModel',
//		scope: true,
		link: function($scope, elm, attrs, ctrl) {
			logger.log("LINK");
			if (!ctrl)
				return;
			var destValue = null;
			$scope.$watch(attrs.mySameValueAs, function(value) {
				destValue = value;
				ctrl.$validate();
			});
			
			Validator.messageId = "my-same-value-as";
			Validator.message = "Values do not match";
			function Validator(modelValue, viewValue) {
				return viewValue == destValue;
				//return (!ctrl.$dirty) || (viewValue == destValue);
			}
			ctrl.$validators.mySameValue = Validator;
		}
	};
}

module.directive('mySameValueAs', Implementation);
