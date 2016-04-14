// http://www.sitepoint.com/using-requirejs-angularjs-applications/
define("main", ["slavi-log", "ang"], function(sl) {
	log("enter");

	$(document).ready(function() {
		$("<pre>").text("Done.").appendTo($("#container"));
	});

	function log(message) {
		if (typeof message !== "string")
			message = JSON.stringify(message);
		console.log(message);
	}
	
	var gem = {
		name: "My precious",
		price: 1.23,
		description: "No other like that"
	};
	
	var app = angular.module("store", []);
	app.controller('StoreController', ["$scope", function($scope) {
		$scope.format = 'M/d/yy h:mm:ss a';
		this.product = gem;
	}]);
	app.directive("myDirective", [function() {
		return {
			restrict: "AEC",
			transclude: true,
			scope: {
				product: "="
			},
			template: "Product name {{product.name}} has price <b>{{product.price|currency}}</b><div ng-transclude></div><span>Seem like working</span>",
			link: function($scope) {
				//$scope.product.name = "Kuku";
				//sl.log($scope);
			}
		}
	}]);
	app.directive('myCurrentTime', ['$interval', 'dateFilter', function($interval, dateFilter) {

		function link(scope, element, attrs) {
			// start the UI update process; save the timeoutId for canceling
			var timeoutId = $interval(function() {
				updateTime(); // update DOM
			}, 1000);

			element.on('$destroy', function() {
				$interval.cancel(timeoutId);
			});

			var format;
			function updateTime() {
				element.text(dateFilter(new Date(), format));
			}

			scope.$watch(attrs.myCurrentTime, function(value) {
				format = value;
				updateTime();
			});
		}

		return {
			link: link
		};
	}]);
	
	angular.bootstrap(document, ['store'], {
		strictDi: true,
	});
	
	return {
		log: log
	};
});

var m = require(["main"], function(m) {
	m.log("DONE!");
});

/*
define([],function(){
	function config($routeProvider) {
		$routeProvider.when('/home', {templateUrl: 'templates/home.html', controller: 'ideasHomeController'})
			.when('/details/:id',{templateUrl:'templates/ideaDetails.html', controller:'ideaDetailsController'})
			.otherwise({redirectTo: '/home'});
	}
	config.$inject=['$routeProvider'];

	return config;
});

*/
