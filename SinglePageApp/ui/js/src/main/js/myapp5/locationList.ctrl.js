var module = angular.module('myapp5');

Implementation.$inject = ["$resource", "$timeout", "locationService", "slavi-logger", "slavi-utils"];
function Implementation($resource, $timeout, locationService, logger, utils) {
	var that = this;
	that.query = "";
	that.selectedItem = null;
	that.data = locationService.queryData();
	that.delayedRunner = utils.delayedRunner();
	
	that.isDone = function() {
		return that.delayedRunner.isDone();
	};
	
	that.isSelected = function(item) {
		return (item !== null) && (item === that.selectedItem);
	};
	
	this.onChange = function() {
		that.delayedRunner.run(function() {
			that.selectedItem = null;
			that.data = locationService.queryData(that.query);
		});
	};

	that.onSelect = function(index) {
		if (that.data.item) {
			var item = that.data.item[index];
			// toggle selected item
			that.selectedItem = item === that.selectedItem ? null : item; 
		}
	};
}

module.component('locationList', {
	template:
		'<input type="text" ng-model="$ctrl.query" ng-change="$ctrl.onChange()" /> \
		<button ng-click="$ctrl.onClick()">Click me</button>\
		<table><tr><td>\
		<div ng-repeat="item in $ctrl.data.item" ng-class="{ selected: $ctrl.isSelected(item) }"> \
			<div ng-click="$ctrl.onSelect($index)"> \
				ID: <span>{{item.id}}</span>; location: <span>{{item.name}}</span> \
			</div> \
		</div>\
		</td><td><location-detail ng-if="$ctrl.selectedItem" item="$ctrl.selectedItem"></location-detail></td>\
		</tr></table>',
	controller: Implementation
});
