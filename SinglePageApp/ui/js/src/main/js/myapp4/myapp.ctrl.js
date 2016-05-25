var module = angular.module('myapp4');

module.component('app', {
	template:
		'<nav> \
			<a ng-link="[\'CrisisCenter\']">Crisis Center</a> \
			<a ng-link="[\'Heroes\']">Heroes</a> \
		</nav> \
		<ng-outlet></ng-outlet>',
	$routeConfig: [
		{path: '/crisis-center/...', name: 'CrisisCenter', component: 'crisisCenter', useAsDefault: true},
		{path: '/heroes/...', name: 'Heroes', component: 'heroezzz' }
	]
});
