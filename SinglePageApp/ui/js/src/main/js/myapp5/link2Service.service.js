var module = angular.module('myapp5');

Service.$inject = ["$q", "slavi-logger"];
function Service($q, logger) {
	var that = this;
	that.isDone = false;
	that.items = [];
	var defered = null;
	
	this.load = function(max) {
		that.isDone = false;
		if (defered !== null)
			defered.resolve();
		defered = $q.defer();
		setTimeout(function() {
			var r = [];
			for (var i = 1; i <= max; i++) {
				var item = { id: i, name: "Name " + i };
				r.push(item);
			}
			defered.resolve(r);
		}, 3000);
		
		defered.promise.then(function(items) {
			that.items = items;
			that.isDone = true;
			logger.log(that.items);
		}, function(rejectReason) {
			logger.log(rejectReason);
		});
	};
	that.load(3);
}

module.service('link2Service', Service);
