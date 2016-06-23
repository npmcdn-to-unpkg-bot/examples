var module = angular.module('myapp5');

Controller.$inject = ["$location", "$translate", "link1Service", "slavi-logger"];
function Controller($location, $translate, service, logger) {
	var that = this;
	var selectedId = null;
	
	that.lang = "en";
	that.allLangs = ["en", "bg"];
	
	that.onChangeLang = function() {
		$translate.use(that.lang);
	};
	
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
