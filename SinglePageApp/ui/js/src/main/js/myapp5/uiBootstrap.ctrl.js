var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "slavi-logger"];
function Implementation($scope, logger) {
	$scope.oneAtATime = true;

	$scope.groups = [{
		title: 'Dynamic Group Header - 1',
		content: 'Dynamic Group Body - 1'
	}, {
		title: 'Dynamic Group Header - 2',
		content: 'Dynamic Group Body - 2'
	}];
}

module.component('uiBootstrap', {
	templateUrl: 'myapp5/uiBootstrap.html',
	controller: Implementation
});
