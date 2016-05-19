// http://www.sitepoint.com/using-requirejs-angularjs-applications/
define("main", ["ang"], function(sl) {
	// $(document).ready(function() { });
/*
	var gem = {
		name: "My precious",
		price: 1.23,
		description: "No other like that",
	};
	gem.dummy = gem["not existing"];
	
	var app = angular.module("store", []);
	app.controller('StoreController', ["$scope", "MySuperService", function($scope, myService) {
		$scope.format = 'M/d/yy h:mm:ss a';
		this.product = gem;
		this.data = "DATA " + myService.getMyNum();
	}]);

	function MyLauncher(myNum) {
		var myNum = "---- " + myNum + " ----";
		
		this.getMyNum = function() {
			return "My num is " + myNum;
		}
	};
	app.provider("MySuperService", [function() {
		var myNum = 5;
		
		this.setMyNum = function(_myNum) {
			myNum = _myNum;
		};
		
		this.$get = [function() {
			return new MyLauncher(myNum);
		}];
	}]);
	app.config(["MySuperServiceProvider", function(myService) {
		myService.setMyNum("Data 2");
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
	
	sl["app"] = app;*/
	return sl;
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
