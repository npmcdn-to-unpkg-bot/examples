/*
// http://www.sitepoint.com/using-requirejs-angularjs-applications/
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
