angular.element(document).ready(function() {
var m = require(["jquery", "ang"], function($, m) {
	angular.module('myapp5', [])
	.component('myapp', {
		template:
			"<nav> \
				<a ng-link='Link1'>Link 1</a> \
				<a ng-link='Link2'>Link 2</a> \
			</nav>"
	});

	angular.bootstrap(document, ['myapp5'], {
		strictDi: true,
	});
})
});


