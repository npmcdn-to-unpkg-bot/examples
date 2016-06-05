var module = angular.module('myapp5');

Implementation.$inject = ["slavi-logger"];
function Implementation(logger) {
	var that = this;

	that.onDone = function() {
		
	};
}

module.component('locationDetail', {
	template: '<div ng-if="$ctrl.item">\
		<h3>Details</h3>\
		<div><label>ID:</label>{{$ctrl.item.id}}</div>\
		<div><label>Name:</label><span>{{$ctrl.item.name}}</span></div>\
		<button ng-click="$ctrl.onDone()">Done</button>\
		</div>',
	bindings: {
		item: "<"
	},
	controller: Implementation
});
