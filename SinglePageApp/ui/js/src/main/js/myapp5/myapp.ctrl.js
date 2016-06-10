var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$route", "$routeParams", "$location", "slavi-logger"];
function Implementation($scope, $route, $routeParams, $location, logger) {
	var that = this;
	
	that.items = [
		{ url: '/search',		label: 'Search' },
		{ url: '/link1',		label: 'Link 1' },
		{ url: '/link2',		label: 'Link 2' },
		{ url: '/location',		label: 'Location' },
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
	template:
		'<div class="container"> \
			<div class="row"> \
				<h1 class="title">My Component Router</h1>\
				<nav class="navbar navbar-default"> \
					<div class="navbar-header"> \
		 				<a class="navbar-brand" href="#">App5</a>\
					</div>\
					<div class="container-fluid"> \
						<ul class="nav navbar-nav"> \
							<li	ng-repeat="item in $ctrl.items" \
								ng-class="{ active: $ctrl.isActive(item) }">\
								<a href="" ng-click="$ctrl.onClick(item)">{{item.label}}</a>\
							</li> \
						</ul>\
					</div>\
				</nav> \
			</div>\
			<div class="row">\
				<div ng-view></div> \
			</div>\
		</div>',
	controller: Implementation
});
