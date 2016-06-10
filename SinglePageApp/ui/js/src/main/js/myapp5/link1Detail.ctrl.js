var module = angular.module('myapp5');

Controller.$inject = ["$scope", "$location", "$routeParams", "link1Service", "slavi-utils"];
function Controller($scope, $location, $routeParams, service, utils) {
	var that = this;
	service.getItem($routeParams.id).then(function(item) {
		that.item = item;
	});

	that.done = function() {
		var itemId = that.item && that.item.id;
		utils.navigateTo("..");
	};
}

module.component('link1Detail', {
	templateUrl: "myapp5/link1Detail.html",
	bindings: {
//		$router: "<"
	},
	controller: Controller
});
