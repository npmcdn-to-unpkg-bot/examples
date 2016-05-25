var module = angular.module('myapp5');

Controller.$inject = ["link1Service", "slavi-logger"];
function Controller(service, logger) {
	var $ctrl = this;
	var selectedId = null;
	
	this.$routerOnActivate = function(next) {
		logger.log(next);
		selectedId = next.params.id;
		service.getItems().then(function(items) {
			$ctrl.items = items;
		});
	};
	
	this.isSelected = function(item) {
		return item.id == selectedId;
	};
}

module.component('link1List', {
	template:
		'<div ng-repeat="item in $ctrl.items" ng-class="{ selected: $ctrl.isSelected(item) }"> \
			<a ng-link="[\'Link1_Detail\', {id: item.id}]">{{item.name}}</a> \
		</div>',
	controller: Controller
});
