var module = angular.module('myapp5');

Implementation.$inject = ["$q", "$parse", "slavi-logger"];
function Implementation($q, $parse, logger) {
	
	logger.log("asyncValidate initialized");
	
	return {
		restrict: 'A',
		require: '?ngModel',
		scope: false,
		link: function($scope, elm, attrs, ctrl) {
			logger.log("asyncValidate link");
			if (!ctrl)
				return;

			console.log(attrs.asyncValidate);
			var validationFn = $parse(attrs.asyncValidate); //, /* interceptorFn */ null, /* expensiveChecks */ true);
			console.log(validationFn.toSource());
			
			Validator.messageId = "my-async-validator";
			Validator.message = "Async validation failed";
			function Validator(modelValue, viewValue) {
				logger.log("asyncValidate Validator");
				var def = $q.defer();
				var callback = function() {
					logger.log("asyncValidate callback");
					try {
						validationFn($scope, {
							modelValue: modelValue, 
							viewValue: viewValue
						});
						def.resolve();
					} catch(e) {
						def.reject(e);
						throw e;
					}
				};
				$scope.$evalAsync(callback);
				return def.promise;
			}

			attrs.$observe("asyncValidate", function(value) {
				validationFn = value;
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

