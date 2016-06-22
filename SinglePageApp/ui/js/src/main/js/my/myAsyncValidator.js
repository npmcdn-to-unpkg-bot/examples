var module = angular.module('my');

/**
 * Usage example:
 * 
 * <my-form-field data-label="My field:" data-name="myFieldName">
 *     <input 
 *         type="text" 
 *         name="myFieldName"
 *         ng-model="item.name"
 *         my-async-validator="$ctrl.asyncValidatorFunction"
 *     />
 * </my-form-field>
 * 
 * app.component("...", {
 *     controller: function() {
 *         this.asyncValidatorFunction = function(newValue) {
 *            if (newValue has some errors) {
 *                return "A nice user friendly error message.";
 *            }
 *            return ""; // or any falsy object.
 *         }
 *         
 *         // ... or return a Promise. If the promise is resolved with a falsy object 
 *         // the validation is ok otherwise or in case of exception the validation fails.
 *         
 *         this.asyncValidatorFunction = function(newValue) {
 *            return $timeout(function() {
 *                if (newValue has some errors) {
 *                    return "A nice user friendly error message.";
 *                }
 *                return ""; // or any falsy object.
 *            });
 *         }
 *     }
 * });
 */

var defaultMessage = "Validation failed";

Implementation.$inject = ["$q", "$parse", "$timeout"];
function Implementation($q, $parse, $timeout) {
	return {
		restrict: 'A',
		require: '?ngModel',
		scope: false,
		link: function($scope, elm, attrs, ctrl) {
			if (!ctrl)
				return;

			var validationFn = angular.noop;
			Validator.message = defaultMessage;
			function Validator(modelValue, viewValue) {
				var def = $q.defer();
				$timeout(function() {
					try {
						Validator.message = defaultMessage;
						var r = validationFn.call(ctrl, modelValue, viewValue);
						$q.when(r, function(msg) {
							if (msg) {
								Validator.message = String(msg);
								def.reject();
							} else {
								def.resolve();
							}
						}, function(e) {
							def.reject();
							throw e;
						});
					} catch(e) {
						def.reject();
						throw e;
					}
				});
				return def.promise;
			}

			$scope.$watch(attrs.myAsyncValidator, function(value) {
				if (typeof value !== "function") {
					value = angular.noop;
				}
				validationFn = value;
			});
			ctrl.$asyncValidators.asyncValidator = Validator;
		}
	};
}

module.directive('myAsyncValidator', Implementation);
