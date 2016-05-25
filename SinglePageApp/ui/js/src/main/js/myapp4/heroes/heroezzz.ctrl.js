var module = angular.module('myapp4.heroes');

module.component('heroezzz', {
	template: '<h2>Heroes</h2><ng-outlet></ng-outlet>',
	$routeConfig: [
		{path: '/',    name: 'HeroList',   component: 'heroList', useAsDefault: true},
		{path: '/:id', name: 'HeroDetail', component: 'heroDetail'}
	]
});
