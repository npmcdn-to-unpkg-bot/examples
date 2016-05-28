var module = angular.module('myapp5');

Controller.$inject = ["link1Service"];
function Controller(service) {
	var $ctrl = this;

	this.$routerOnActivate = function(next) {
		var id = next.params.id;
		service.getItem(id).then(function(item) {
			$ctrl.item = item;
		});
	};
	
	this.done = function() {
		var itemId = $ctrl.item && $ctrl.item.id;
		$ctrl.$router.navigate(['Link1_List', {id: itemId}]);
	};
}

module.component('link1Detail', {
	template: '<div ng-if="$ctrl.item">\
		<h3>Details</h3>\
		<div><label>ID:</label>{{$ctrl.item.id}}</div>\
		<div><label>Name:</label><input ng-model="$ctrl.item.name" placeholder="name" /></div>\
		<button ng-click="$ctrl.done()">Done</button>\
		</div>',
	bindings: {
		$router: "<"
	},
	controller: Controller
});
