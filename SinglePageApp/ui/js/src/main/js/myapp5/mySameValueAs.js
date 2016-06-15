var module = angular.module('myapp5');

Implementation.$inject = [];
function Implementation() {
	return {
		restrict: 'A',
		require: '?ngModel',
		scope: false,
		link: function($scope, elm, attrs, ctrl) {
			if (!ctrl)
				return;
			var destValue = null;
			$scope.$watch(attrs.mySameValueAs, function(value) {
				destValue = value;
				ctrl.$validate();
			});
		
			Validator.messageId = "my-same-value-as";
			Validator.message = "Value does not match";
			function Validator(modelValue, viewValue) {
				return viewValue == destValue;
				//return (!ctrl.$dirty) || (viewValue == destValue);
			}
			ctrl.$validators.mySameValue = Validator;
		}
	};
}

module.directive('mySameValueAs', Implementation);
