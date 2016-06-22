var module = angular.module('myapp5');

Service.$inject = ["$q"];
function Service($q) {
	var dataPromise = $q.when([
		{ id: 1, name: "Name 1"},
		{ id: 2, name: "Name 2"},
		{ id: 3, name: "Name 3"},
		{ id: 4, name: "Name 4"},
	]);
	
	this.getItems = function() {
		return dataPromise;
	};
	
	this.getItem = function(id) {
		return dataPromise.then(function(data) {
			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				if (item.id == id) {
					return item;
				}
			}
		});
	};
}

module.service('link1Service', Service);
