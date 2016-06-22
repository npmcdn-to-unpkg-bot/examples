var module = angular.module('myapp4.crisis-center');

module.component('crisisCenter', {
	template: '<h2>Crisis Center</h2><ng-outlet></ng-outlet>',
	$routeConfig: [
		{path:'/',    name: 'CrisisList',   component: 'crisisList', useAsDefault: true},
		{path:'/:id', name: 'CrisisDetail', component: 'crisisDetail'}
	]
});
