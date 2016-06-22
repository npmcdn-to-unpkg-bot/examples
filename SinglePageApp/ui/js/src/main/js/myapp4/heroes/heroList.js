var module = angular.module('myapp4.heroes');

Controller.$inject = ["heroService", "slavi-logger"];
function Controller(service, logger) {
	var selectedId = null;
	var $ctrl = this;
	
	this.$routerOnActivate = function(next) {
		// Load up the heroes for this view
		service.getHeroes().then(function(heroes) {
			$ctrl.heroes = heroes;
			selectedId = next.params.id;
		});
	};
	
	this.isSelected = function(hero) {
		return (hero.id == selectedId);
	};
}

module.component('heroList', {
	template:
		'<div	ng-repeat="hero in $ctrl.heroes" \
				ng-class="{ selected: $ctrl.isSelected(hero) }"> \
			<a ng-link="[\'HeroDetail\', {id: hero.id}]">{{hero.name}}</a>\
		</div>',
	controller: Controller
});
