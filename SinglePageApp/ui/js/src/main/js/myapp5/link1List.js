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
	templateUrl: "myapp5/link1List.html",
	controller: Controller
});
