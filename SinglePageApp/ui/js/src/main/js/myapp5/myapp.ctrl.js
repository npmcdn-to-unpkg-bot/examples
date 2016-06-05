var module = angular.module('myapp5');

module.component('myapp', {
	template:
		'<nav> \
			<a ng-link="[\'Search\']">Search</a> \
			<a ng-link="[\'Link1\']">Link 1</a> \
			<a ng-link="[\'Link2\']">Link 2</a> \
			<a ng-link="[\'Location\']">Location</a> \
		</nav> \
		<ng-outlet></ng-outlet>',
	$routeConfig: [
		{ path: '/search/...', name: 'Search', component: 'search' },
		{ path: '/link1/...', name: 'Link1', component: 'link1', useAsDefault: true },
		{ path: '/link2', name: 'Link2', component: 'link2' },
		{ path: '/location/...', name: 'Location', component: 'location' }
	]
});
