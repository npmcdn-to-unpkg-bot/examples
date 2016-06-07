var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "slavi-logger"];
function Implementation($scope, logger) {
	var that = this;
	$scope.$watch("$ctrl.item", function(newValue) {
		$scope.item = angular.copy(newValue);
	});
	
	that.onDone = function() {
		angular.merge($scope.$ctrl.item, $scope.item);
	};
}

module.component('locationDetail', {
	template: '\
		<h3>Details</h3>\
		<div><label>ID:</label>{{item.id}}</div>\
		<div><input type="text" ng-model="item.name"</div>\
		<button ng-click="$ctrl.onDone()">Done</button>',
	bindings: {
		item: "<"
	},
	controller: Implementation
});
