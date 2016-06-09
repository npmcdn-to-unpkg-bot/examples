var module = angular.module('myapp5');

Controller.$inject = ["$scope", "$routeParams", "link1Service"];
function Controller($scope, $routeParams, service) {
	var $ctrl = this;
	service.getItem($routeParams.id).then(function(item) {
		$ctrl.item = item;
	});

	this.done = function() {
		var itemId = $ctrl.item && $ctrl.item.id;
		$scope.log("Done ", itemId);
//		$ctrl.$router.navigate(['Link1_List', {id: itemId}]);
	};
}

module.component('link1Detail', {
	template: '<div ng-if="$ctrl.item">\
		<h3>Details</h3>\
		<div><label>ID:</label>{{$ctrl.item.id}}</div>\
		<div><label>Name:</label><input ng-model="$ctrl.item.name" placeholder="name" /></div>\
		<button ng-click="$ctrl.done()">Done</button>\
		</div>',
	bindings: {
//		$router: "<"
	},
	controller: Controller
});
