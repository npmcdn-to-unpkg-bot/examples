var module = angular.module('myapp5');

module.component('link1', {
	template: "<h2>This is LINK 1</h2><ng-outlet></ng-outlet>",
	$routeConfig: [
		{path: '/',    name: 'Link1_List',   component: 'link1List', useAsDefault: true},
		{path: '/:id', name: 'Link1_Detail', component: 'link1Detail'}
	]
});
