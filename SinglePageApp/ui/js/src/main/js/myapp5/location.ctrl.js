var module = angular.module('myapp5');

module.component('location', {
	template: "<h2>Location</h2><ng-outlet></ng-outlet>",
	$routeConfig: [
		{path: '/',    name: 'Location_List',   component: 'locationList', useAsDefault: true},
//		{path: '/:id', name: 'Search_Detail', component: 'searchDetail'}
	]
});
