var module = angular.module('myapp5');

module.component('myapp', {
	template:
		'<nav> \
			<a ng-link="[\'Link1\']">Link 1</a> \
			<a ng-link="[\'Link2\']">Link 2</a> \
		</nav> \
		<ng-outlet></ng-outlet>',
	$routeConfig: [
		{path: '/link1/...', name: 'Link1', component: 'link1', useAsDefault: true},
		{path: '/link2', name: 'Link2', component: 'link2' }
	]
});
