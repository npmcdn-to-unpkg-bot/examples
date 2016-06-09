var module = angular.module('myapp5');

Controller.$inject = ["$location", "link1Service", "slavi-logger"];
function Controller($location, service, logger) {
	var that = this;
	var selectedId = null;
	
	service.getItems().then(function(items) {
		that.items = items;
	});

	that.onClick = function(item) {
		$location.path($location.path() + "/" + item.id);
	};

	that.isSelected = function(item) {
		return item.id == selectedId;
	};
}

module.component('link1List', {
	template:
		'<h2>This is LINK 1</h2>\
		<div ng-repeat="item in $ctrl.items" ng-class="{ \'alert-info\': $ctrl.isSelected(item) }"> \
			<a href="" ng-click="$ctrl.onClick(item)">{{item.name}}</a> \
		</div>',
	controller: Controller
});
