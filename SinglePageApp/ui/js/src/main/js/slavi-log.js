define("slavi-log", ["angular"], function(a) {
	var module = angular.module("slavi-log", []);
	
	module.service("logger", [ function() {
		var that = this;
		that.maxLines = 10;
		that.consoleLogEnabled = true;
		that.items = [];
		
		that.isEmpty = function() {
			return that.items.length == 0;
		};

		that.clear = function() {
			that.items.slipce(0, that.items.length);
		};
		
		that.stringify = function(obj) {
			seen = [];
			return JSON.stringify(obj, function(key, val) {
				if (val != null && typeof val === "object") {
					if (seen.indexOf(val) >= 0) {
						return "... cyclic ref ...";
					}
					seen.push(val);
				}
				return val;
			});
		};
		
		that.log = function() {
			var message = "";
			for (var i = 0; i < arguments.length; i++) {
				var a = arguments[i];
				if (typeof a !== "string")
					a = that.stringify(a);
				message = message + a;
			}
			that.items.push(message);
			if (consoleLogEnabled) {
				console.log(message);
			}
			var remove = that.items.length - that.maxLines;
			if (remove > 0) {
				that.items.splice(0, remove);
			}
		}
	}]);

	module.component("log", { 
		template: "<div ng-hide='$ctrl.isEmpty()' ng-repeat='item in $ctrl.items'><pre>{{item}}</pre></div>",
		controller: ["logger", function(logger) {
			this.items = logger.items;
			this.isEmpty = logger.isEmpty;
		}]
	});
	
	var injector = angular.element(document).injector();
	// log is a global variable/function
	log = injector.get("logger").log;
	
	return log;
});
