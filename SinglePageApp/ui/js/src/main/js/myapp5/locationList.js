var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$resource", "$routeParams", "$q", "$timeout", "locationService", "$parse", "slavi-logger", "slavi-utils"];
function Implementation($scope, $resource, $routeParams, $q, $timeout, service, $parse, logger, utils) {
	var that = this;
	that.service = service;
	$scope.service = service;
	that.delayedRunner = utils.delayedRunner();
	
	that.isDone = function() {
		return that.delayedRunner.isDone();
	};
	
	that.invokeMe = function() {
		logger.log("invokeMe 1");
		
		var def = $q.defer();
		
		var p = $timeout(function() {
			return 0; //"some value";
		}, 1000);
		$q.when(p, function(v) {
			logger.log("--- Success ", v);
			if (!v) {
				def.reject();
			}
			def.resolve();
		}, function(v) {
			logger.log("--- Error ", v);
			def.reject();
		});
		logger.log("invokeMe 2");
		
		def.promise.then(function(v) {
			logger.log("Success ", v);
		}, function(v) {
			logger.log("Failure ", v);
		});
		logger.log("invokeMe 3");
	};
	
	that.onClick = function() {
		logger.log("onClick");
		var fn = $parse("$ctrl.invokeMe()");
		fn($scope, {});
	};
	
	that.query = "";
	that.page = 1;
	that.size = 5;
	that.data = {};
	that.orderBy = [
		{ k: "+name",		v: "Name asc" },
		{ k: "-name",		v: "Name desc" },
		{ k: "+id",			v: "ID asc" },
		{ k: "-id",			v: "ID desc" },
	];
	that.order = that.orderBy[0].v;
	
	that.updateList = function() {
		service.selectedItem = null;
		var r = service.resource.search({ search: that.query, page: that.page-1, size: that.size, order: that.order });
		r.$promise.then(function() {
			that.data = r;
			console.log(r);
		});
	};
	that.updateList();
	
	that.dummy = function() {
		console.log(that.order);
	};
	
	that.onSelect = function(item) {
		// toggle selected item
		service.selectedItem = item === service.selectedItem ? null : item;
	};
}

module.component("locationList", {
	templateUrl: "myapp5/locationList.html",
	controller: Implementation
});

/*
module.directive('notEndingWithA', function() {
	return {
		require: 'ngModel',
		link: function(scope, elm, attrs, ctrl) {
			console.log("Init");
			ctrl.$validators.notEndingWithA = function(modelValue, viewValue) {
				console.log("Validate " + viewValue);
				return !String(viewValue).endsWith("a");
			};
		}
	};
});*/
