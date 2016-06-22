var module = angular.module('my');

module.component('myPanel', {
	templateUrl: "my/myPanel.html",
	transclude: true,
	bindings: {
		title: "@"
	}
});
