var module = angular.module('myapp5');

Controller.$inject = ["slavi-logger"];
function Controller(logger) {
	var $ctrl = this;

	
}

module.component('searchDetail', {
	templateUrl: "myapp5/searchDetail.html",
	bindings: {
		item: "<"
	},
	controller: Controller
});
