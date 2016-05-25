var module = angular.module('myapp2');

Controller.$inject = ["$scope", "$element", "$attrs", "slavi-logger"];
function Controller($scope, $element, $attrs, logger) {
	var $ctrl = this;
	logger.log("hi");
}

module.component('myComponent', {
	transclude: true,
	template:
		"<p>MY COMPONENT: Product name {{$ctrl.product.name}} has price \
			<b>{{$ctrl.product.price|currency}}</b> \
			<div ng-transclude></div>\
			<span>Seem like working</span>\
		</p>",
	bindings: { product: "=" },
	controller: Controller
});
