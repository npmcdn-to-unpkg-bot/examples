var module = angular.module('myapp5');

module.component('myapp', {
	template:
		'<div class="container"> \
			<div class="row"><div class="col-xs-12">\
				<h1 class="title">My Component Router</h1>\
				<nav> \
					<a ng-link="[\'Search\']">Search</a> \
					<a ng-link="[\'Link1\']">Link 1</a> \
					<a ng-link="[\'Link2\']">Link 2</a> \
					<a ng-link="[\'Location\']">Location</a> \
				</nav> \
			</div></div> \
			<ng-outlet></ng-outlet> \
		</div>',
	$routeConfig: [
		{ path: '/search/...', name: 'Search', component: 'search' },
		{ path: '/link1/...', name: 'Link1', component: 'link1', useAsDefault: true },
		{ path: '/link2', name: 'Link2', component: 'link2' },
		{ path: '/location/...', name: 'Location', component: 'location' }
	]
});
