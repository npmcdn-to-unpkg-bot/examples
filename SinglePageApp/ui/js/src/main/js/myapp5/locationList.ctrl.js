var module = angular.module('myapp5');

Implementation.$inject = ["$resource", "$routeParams", "$timeout", "locationService", "slavi-logger", "slavi-utils"];
function Implementation($resource, $routeParams, $timeout, locationService, logger, utils) {
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

	that.onSelect = function(item) {
		// toggle selected item
		that.selectedItem = item === that.selectedItem ? null : item;
	};
}

module.component('locationList', {
	template:
		'<div class="col-sm-4"> \
			<div class="panel panel-default">\
				<div class="panel-heading">Location</div>\
				<div class="panel-body"> \
					<div class="row">\
						<div class="col-sm-12"> \
							<input type="text" class="form-control" ng-model="$ctrl.query" ng-change="$ctrl.onChange()" /> \
						</div>\
					</div>\
					<div class="row">\
						<div class="col-sm-12"> \
							<div ng-repeat="item in $ctrl.data.item" ng-class="{ \'alert-info\': $ctrl.isSelected(item) }"> \
								<div ng-click="$ctrl.onSelect(item)"> \
									ID: <span>{{item.id}}</span>; location: <span>{{item.name}}</span> \
								</div> \
							</div> \
						</div>\
					</div>\
				</div>\
			</div>\
		</div> \
		<div class="col-sm-8"> \
			<location-detail ng-if="$ctrl.selectedItem" item="$ctrl.selectedItem"></location-detail> \
		</div>',
	controller: Implementation
});
