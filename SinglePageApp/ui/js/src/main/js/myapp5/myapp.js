var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$route", "$routeParams", "$location", "slavi-logger"];
function Implementation($scope, $route, $routeParams, $location, logger) {
	var that = this;
	
	that.items = [
		{ url: '/search',		label: 'Search' },
		{ url: '/link1',		label: 'Link 1' },
		{ url: '/link2',		label: 'Link 2' },
		{ url: '/location',		label: 'Location' },
		{ url: '/myroute',		label: 'My route' },
		{ url: '/uiBootstrap',	label: 'UI Bootstrap' }
	];

	that.isActive = function(item) {
		return $location.path().indexOf(item.url) === 0;
	};
	
	that.onClick = function(item) {
		$location.path(item.url);
	};
}

module.component('myapp', {
	templateUrl: "myapp5/myapp.html",
	controller: Implementation
});
