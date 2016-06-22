var module = angular.module('myapp4.crisis-center');

Controller.$inject = ["crisisService", "slavi-logger"];
function Controller(service, logger) {
	var selectedId = null;
	var ctrl = this;
	
	this.$routerOnActivate = function(next) {
		console.log('$routerOnActivate', this, arguments);
		// Load up the crises for this view
		service.getCrises().then(function(crises) {
			ctrl.crises = crises;
			selectedId = next.params.id;
		});
	};
	
	this.isSelected = function(crisis) {
		return (crisis.id == selectedId);
	};
	
	this.onSelect = function(crisis) {
		this.$router.navigate(['CrisisDetail', { id: crisis.id }]);
	};
}

module.component('crisisList', {
	template:
		'<ul>\
			<li	ng-repeat="crisis in $ctrl.crises"\
				ng-class="{ selected: $ctrl.isSelected(crisis) }"\
				ng-click="$ctrl.onSelect(crisis)">\
				<span class="badge">{{crisis.id}}</span> {{crisis.name}}\
			</li>\
		</ul>',
	bindings: { $router: '<' },
	controller: Controller,
	$canActivate: ["$nextInstruction", "$prevInstruction", "slavi-logger", function($nextInstruction, $prevInstruction, logger) {
		logger.log('$canActivate', arguments);
	}]
});
