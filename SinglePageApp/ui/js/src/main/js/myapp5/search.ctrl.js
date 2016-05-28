var module = angular.module('myapp5');

module.component('search', {
	template: "<h2>Search</h2><ng-outlet></ng-outlet>",
	$routeConfig: [
		{path: '/',    name: 'Search_List',   component: 'searchList', useAsDefault: true},
//		{path: '/:id', name: 'Search_Detail', component: 'searchDetail'}
	]
});
