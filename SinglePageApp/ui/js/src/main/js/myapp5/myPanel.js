var module = angular.module('myapp5');

module.component('myPanel', {
	templateUrl: "myapp5/myPanel.html",
	transclude: true,
	bindings: {
		title: "="
	}
});
