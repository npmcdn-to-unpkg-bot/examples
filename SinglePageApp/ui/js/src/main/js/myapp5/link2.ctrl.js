var module = angular.module('myapp5');

Implementation.$inject = ["link2Service", "$scope", "slavi-logger"];
function Implementation(service, $scope, logger) {
	$scope.$service = service;
	logger.log(service.items);
	
	$scope.even = function(item) {
		return item.id % 2 === 0;
	};
	
	/* this.$routerOnActivate = function(next) {
		//logger.log()
	} */
}

module.component('link2', {
	templateUrl: "myapp5/link2.html",
	controller: Implementation
});
