var module = angular.module('myapp5');

/**
 * Usage example:
 * 
 * <my-form-field data-label="My field:" data-name="myFieldName">
 *     <input 
 *         type="text" 
 *         name="myFieldName"
 *         ng-model="item.name"
 *         my-validator="$ctrl.validatorFunction"
 *     />
 * </my-form-field>
 * 
 * app.component("...", {
 *     controller: function() {
 *         this.validatorFunction = function(newValue) {
 *            if (newValue has some errors) {
 *                return "A nice user friendly error message.";
 *            }
 *            return ""; // or any falsy object.
 *         }
 *     }
 * });
 */

Implementation.$inject = ["$q", "$parse"];
function Implementation($q, $parse) {
	return {
		restrict: 'A',
		require: '?ngModel',
		scope: false,
		link: function($scope, elm, attrs, ctrl) {
			if (!ctrl)
				return;

			var validationFn = angular.noop;
			Validator.message = "";
			function Validator(modelValue, viewValue) {
				var msg = validationFn.call(ctrl, viewValue);
				if (msg) {
					Validator.message = String(msg);
					return false;
				}
				return true;
			}

			$scope.$watch(attrs.myValidator, function(value) {
				if (typeof value !== "function") {
					value = angular.noop;
				}
				validationFn = value;
			});
			
			ctrl.$validators.myValidator = Validator;
		}
	};
}

module.directive('myValidator', Implementation);
