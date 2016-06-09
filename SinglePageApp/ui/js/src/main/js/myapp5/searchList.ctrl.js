var module = angular.module('myapp5');

Controller.$inject = ["$resource", "$timeout", "slavi-logger"];
function Controller($resource, $timeout, logger) {
	var $ctrl = this;
	$ctrl.query = "BG";
	$ctrl.selectedItem = null;
	
	var appid = "dj0yJmk9N2FQeDlGT1VUNjFrJmQ9WVdrOU4wbElSWEJITkc4bWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1jMA--";
	// http://where.yahooapis.com/v1/places.q(BG);count=5?format=json&appid=dj0yJmk9N2FQeDlGT1VUNjFrJmQ9WVdrOU4wbElSWEJITkc4bWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1jMA--
	
	var search = $resource("http://where.yahooapis.com/v1/places.q(:q);count=:count", {
		appid: appid,
		count: 5,
		q: "BG",
		callback: "JSON_CALLBACK",
		format: "json"
	}, {
		get: { method: "jsonp" }
	}, {
		stripTrailingSlashes: false,
		cancellable: true
	});
	$ctrl.data = null;
	
	this.queryData = function() {
		if ($ctrl.query === "") {
			return;
		}
		$ctrl.selectedItem = null;
		if ($ctrl.data !== null) {
			$ctrl.data.$cancelRequest();
		}
		$ctrl.data = search.get({ q: $ctrl.query });
	};
	this.queryData();
	
	this.isLoadingData = function() {
		if ($ctrl.data === null)
			return false;
		return !$ctrl.data.$promise.$resolved;
	};
	
	this.isSelected = function(item) {
		return (item !== null) && (item === $ctrl.selectedItem);
	};
	
	this.onClick = function() {
		if (!(
			($ctrl.data) &&
			($ctrl.data.places) &&
			($ctrl.data.places.place))) {
			logger.log("No data");
		}
		var selected = [];
		for (var i = 0; i < $ctrl.data.places.place.length; i++) {
			var item = $ctrl.data.places.place[i];
			if (item.selected) {
				selected.push(item.woeid);
			}
		}
		logger.log("Selected items:", selected);
	};
	
	var changePromise = null;
	var delay = 700;
	this.onChange = function() {
		if (changePromise !== null) {
			$timeout.cancel(changePromise);
		}
		changePromise = $timeout(function() {
			changePromise = null;
			$ctrl.queryData();
		}, delay);
	};
	
	this.onSelect = function(index) {
		if ((!$ctrl.data) ||
			(!$ctrl.data.places) ||
			(!$ctrl.data.places.place))
			return;
		var item = $ctrl.data.places.place[index];
		$ctrl.selectedItem = item === $ctrl.selectedItem ? null : item;
	};
}

module.component('searchList', {
	template:
		'<h2>Search</h2>\
		<input type="text" ng-model="$ctrl.query" ng-change="$ctrl.onChange()" /> \
		<button ng-click="$ctrl.onClick()">Click me</button>\
		<table><tr><td>\
		<div ng-repeat="item in $ctrl.data.places.place" ng-class="{ selected: $ctrl.isSelected(item) }"> \
			<div ng-click="$ctrl.onSelect($index)"> \
				<input type="checkbox" ng-model="item.selected" />\
				<h4>{{item.name}}</h4> \
				WOEID: <span>{{item.woeid}}</span>; country: <span>{{item.country}}</span> \
			</div> \
		</div>\
		</td><td><search-detail item="$ctrl.selectedItem"></search-detail></td>\
		</tr></table>',
	controller: Controller
});
