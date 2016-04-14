// http://www.sitepoint.com/using-requirejs-angularjs-applications/
define("main", ["jquery", "slavi-log", "ang"], function($, sl) {
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
	app.controller("StoreController", ["$scope", function($scope) {
		this.product = gem;
		$scope.bla = gem;
	}]);
	app.directive("myDirective", function() {
		return {
			restrict: "A",
			scope: {
				myData: "=",
			},
			template: "Bla bla <b>tra la la</b>!!!... {{myData.name}}",
		};
	});
	
	angular.bootstrap(document, ["store"]);

	return {
		log: log
	};
});

var mainApp;
require(["main"], function(m) {
	mainApp = m;
	m.log("DONE!");
});

/*
define([],function(){
	function config($routeProvider) {
		$routeProvider.when("/home", {templateUrl: "templates/home.html", controller: "ideasHomeController"})
			.when("/details/:id",{templateUrl:"templates/ideaDetails.html", controller:"ideaDetailsController"})
			.otherwise({redirectTo: "/home"});
	}
	config.$inject=["$routeProvider"];

	return config;
});
*/

