var module = angular.module('myapp5');

Controller.$inject = ["slavi-logger"];
function Controller(logger) {
	var $ctrl = this;

	
}

module.component('searchDetail', {
	template: '<div ng-if="$ctrl.item">\
		<h3>Details</h3>\
		<div><label>WOEID:</label>{{$ctrl.item.woeid}}</div>\
		<div><label>Name:</label><span>{{$ctrl.item.name}}</span></div>\
		<button ng-click="$ctrl.done()">Done</button>\
		</div>',
	bindings: {
		item: "<"
	},
	controller: Controller
});
