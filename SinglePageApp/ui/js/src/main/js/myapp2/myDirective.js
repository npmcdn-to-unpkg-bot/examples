var module = angular.module("myapp2");

Controller.$inject = ["MySuperService", "slavi-logger"];
function Controller(myService, logger) {
	return {
		restrict: "AEC",
		transclude: true,
		scope: {
			product: "="
		},
		template: "Product name {{product.name}} has price <b>{{product.price|currency}}</b><div ng-transclude></div><span>Seem like working</span>",
		link: function($scope) {
			//$scope.product.name = "Kuku";
			//logger.log($scope);
		}
	};
}

module.directive("myDirective", Controller);
