var module = angular.module('my');

/**
 * Usage example:
 * 
 * <my-form-field data-label="My field:" data-name="myFieldName1">
 *     <input 
 *         type="text" 
 *         name="myFieldName1"
 *         ng-model="item.name1"
 *     />
 * </my-form-field>
 * <my-form-field data-label="Repeat my field value:" data-name="myFieldName2">
 *     <input 
 *         type="text" 
 *         name="myFieldName2"
 *         ng-model="item.name2"
 *         my-same-value-as="item.name1"
 *         my-same-value-as-message="My message is: {{$ctrl.someMessage}}"
 *     />
 * </my-form-field>
 */

var defaultMessage = "Value does not match";

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
		
			Validator.message = defaultMessage;
			function Validator(modelValue, viewValue) {
				var msg = attrs.mySameValueAsMessage;
				Validator.message = (msg) ? msg : defaultMessage;
				return viewValue == destValue;
			}
			
			attrs.$observe("mySameValueAsMessage", function(msg) {
				Validator.message = (msg) ? msg : defaultMessage;
			});

			ctrl.$validators.mySameValue = Validator;
		}
	};
}

module.directive('mySameValueAs', Implementation);
