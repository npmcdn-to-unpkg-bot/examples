var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$routeParams", "$location", "$timeout"];
function Implementation($scope, $routeParams, $location, $timeout) {
	var that = this;

	$scope.$watch("$ctrl.item", function(newValue) {
	});
	
	var id = $routeParams.id;
	if (id)
		id = id.split("/");
	angular.forEach(id, function(value, key) {
		id[key] = decodeURIComponent(value);
	});
	
	
	console.log(id);
	
	
	
	
/*
	that.onCancel = function() {
		console.log("onCancel");
	};
	
	that.onSave = function() {
		console.log("onSave");
	};*/
}

module.component('myRoute', {
	templateUrl: 'myapp5/myRoute.html',
	controller: Implementation
});
