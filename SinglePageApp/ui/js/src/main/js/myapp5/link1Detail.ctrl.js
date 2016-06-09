var module = angular.module('myapp5');

Controller.$inject = ["$scope", "$location", "$routeParams", "link1Service", "slavi-utils"];
function Controller($scope, $location, $routeParams, service, utils) {
	var that = this;
	service.getItem($routeParams.id).then(function(item) {
		that.item = item;
	});

	that.done = function() {
		var itemId = that.item && that.item.id;
		utils.navigateTo();
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
