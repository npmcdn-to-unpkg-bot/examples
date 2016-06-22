var module = angular.module("slavi-log");
	
LoggerCtrl.$inject = ["slavi-logger"];
function LoggerCtrl(logger) {
	this.items = logger.items;
	this.isEmpty = logger.isEmpty;
}
module.component("log", { 
	template: "<div ng-hide='$ctrl.isEmpty()' ng-repeat='item in $ctrl.items'><pre ng-bind='item' /></div>",
	controller: LoggerCtrl
});

/*
	angular.element(document).ready(function() {
		var injector = angular.element(document).injector();
		log = injector.get("logger").log;
	});
	*/
