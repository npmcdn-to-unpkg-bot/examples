var module = angular.module('myapp5');

module.component('location', {
	template: 
		'<div class="row"> \
			<div class="col-xs-12">\
				<h2>Location</h2> \
			</div> \
		</div> \
		<ng-outlet></ng-outlet>',
	$routeConfig: [
		{path: '/',    name: 'Location_List',   component: 'locationList', useAsDefault: true},
//		{path: '/:id', name: 'Search_Detail', component: 'searchDetail'}
	]
});
