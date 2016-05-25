var module = angular.module('myapp4.heroes');

Controller.$inject = ["heroService", "slavi-logger"];
function Controller(service, logger) {
	var $ctrl = this;
	
	this.$routerOnActivate = function(next) {
		// Get the hero identified by the route parameter
		var id = next.params.id;
		service.getHero(id).then(function(hero) {
			$ctrl.hero = hero;
		});
	};
	
	this.gotoHeroes = function() {
		logger.log("Goto List");
		var heroId = this.hero && this.hero.id;
		this.$router.navigate(['HeroList', {id: heroId}]);
	};
}

module.component('heroDetail', {
	template:
		'<div ng-if="$ctrl.hero">\
			<h3>"{{$ctrl.hero.name}}"</h3>\
			<div><label>Id: </label>{{$ctrl.hero.id}}</div>\
			<div>\
				<label>Name: </label>\
				<input ng-model="$ctrl.hero.name" placeholder="name"/>\
			</div>\
			<button ng-click="$ctrl.gotoHeroes()">Back</button>\
		</div>',
	bindings: { $router: '<' },
	controller: Controller
});
