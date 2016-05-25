var module = angular.module("myapp2");

var gem = {
	name: "My precious",
	price: 1.23,
	description: "No other like that",
};
gem.dummy = gem["not existing"];

Controller.$inject = ["$scope", "MySuperService", "slavi-logger"];
function Controller($scope, myService, logger) {
	$scope.format = 'M/d/yy h:mm:ss a';
	this.product = gem;
	this.data = "DATA " + myService.getMyNum();
}

module.controller("StoreController", Controller);
