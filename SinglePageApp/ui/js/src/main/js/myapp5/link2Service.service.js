var module = angular.module('myapp5');

Implementation.$inject = ["$q", "slavi-logger"];
function Implementation($q, logger) {
	var that = this;
	that.items = [];
	that.deferred = null;
	
	this.load = function(max) {
		if (that.deferred !== null)
			that.deferred.resolve();
		that.deferred = $q.defer();
		setTimeout(function() {
			var r = [];
			for (var i = 1; i <= max; i++) {
				var item = { id: i, name: "Name " + i };
				r.push(item);
			}
			that.deferred.resolve(r);
		}, 3000);
		that.deferred.promise.then(function(items) {
			that.items = items;
			logger.log(that.items);
		}, function(rejectReason) {
			logger.log("Rejected ", rejectReason);
		});
	};
	that.load(3);
}

module.service('link2Service', Implementation);
