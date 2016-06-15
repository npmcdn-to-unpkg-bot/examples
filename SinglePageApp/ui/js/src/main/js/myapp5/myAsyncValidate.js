var module = angular.module('myapp5');

Implementation.$inject = ["$q", "$parse", "slavi-logger"];
function Implementation($q, $parse, logger) {
	return {
		restrict: 'A',
		require: '?ngModel',
		scope: false,
		link: function($scope, elm, attrs, ctrl) {
			if (!ctrl)
				return;

			var validationFn = angular.noop;
			Validator.messageId = "my-async-validator";
			Validator.message = "Async validation failed";
			function Validator(modelValue, viewValue) {
				logger.log("Validator ", modelValue, " ", viewValue);
				var def = $q.defer();
				var callback = function() {
					logger.log("Callback");
					try {
						var validationFn = $parse(attrs.asyncValidate); //, /* interceptorFn */ null, /* expensiveChecks */ true);
						if (validationFn.assign) {
							validationFn = validationFn($scope, {});
							if (typeof validationFn !== "function")
								validationFn = angular.noop;
						} else {
							validationFn = angular.noop;
						}

						var r = validationFn.call(ctrl, modelValue, viewValue);
						def.resolve(r);
					} catch(e) {
						def.reject(e);
						throw e;
					}
				};
				$scope.$eval(callback);
				return def.promise;
			}

			attrs.$observe("asyncValidate", function(value) {
				validationFn = $parse(value); //, /* interceptorFn */ null, /* expensiveChecks */ true);
				if (validationFn.assign) {
					validationFn = validationFn($scope, {});
					if (typeof validationFn !== "function")
						validationFn = angular.noop;
				} else {
					validationFn = angular.noop;
				}
				logger.log("-------------");
				console.log(validationFn);
				// validationFn = $scope[value];
				logger.log(validationFn);
			});
			attrs.$observe("asyncValidateMessageId", function(value) {
				Validator.messageId = value;
			});
			attrs.$observe("asyncValidateMessage", function(value) {
				Validator.message = value;
			});
			ctrl.$asyncValidators.asyncValidaor = Validator;
		}
	};
}

module.directive('asyncValidate', Implementation);

