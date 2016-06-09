var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$route", "$routeParams", "$location", "slavi-logger"];
function Implementation($scope, $route, $location, logger) {
	$scope.$route = $route;
	$scope.$location = $location;
	$scope.log = logger;
	
	var that = this;
	
	that.items = [
		{ link: 'search',		label: 'Search' },
		{ link: 'link1',		label: 'Link 1' },
		{ link: 'link2',		label: 'Link 2' },
		{ link: 'location',		label: 'Location' }
	];
	
	that.selectedItem = null;
	
	that.onClick = function(item) {
		that.selectedItem = item;
	};
	that.isSelected = function(item) {
		return that.selectedItem === item;
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
								ng-class="{ active: $ctrl.isSelected(item) }">\
								<a ng-click="$ctrl.onClick(item)" href="#/{{item.link}}" >{{item.label}}</a>\
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
