var module = angular.module('myapp5');

Controller.$inject = ["link2Service", "$scope", "slavi-logger"];
function Controller(service, $scope, logger) {
	$scope.$service = service;
	logger.log(service.items);
	
	$scope.even = function(item) {
		return item.id % 2 == 0;
	};
	
	/* this.$routerOnActivate = function(next) {
		//logger.log()
	} */
}

module.component('link2', {
	template: 
		'<h2>This is LINK 2</h2>\
		<p ng-hide="$service.isDone">LOADING ...</p>\
		<table ng-show="$service.isDone">\
			<tr ng-repeat="item in $service.items" ng-class="{ selected: $ctrl.even(item) }"> \
				<td>id: {{item.id}}, name: {{item.name}}</td> \
			</tr> \
		</table>\
		<button ng-click="$service.load(4)">Refresh</button>',
	controller: Controller
});
