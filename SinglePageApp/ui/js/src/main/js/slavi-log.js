// log is a global variable/function
var log = function() {
	console.log(arguments)
};

define("slavi-log", ["angular"], function(a) {
	var module = angular.module("slavi-log", []);
	
	function stringify() {
		var result = "";
		for (var i = 0; i < arguments.length; i++) {
			var obj = arguments[i];
			if (typeof obj !== "string") {
				var seen = [];
				obj = JSON.stringify(obj, function(key, val) {
					if (val != null && typeof val === "object") {
						if (seen.indexOf(val) >= 0) {
							return "... cyclic ref ...";
						}
						seen.push(val);
					}
					return val;
				});
			}
			result = result + obj;
		}
		return result;
	};
	
	log = function() {
		console.log(stringify.apply(this, arguments));
	}
	
	module.service("logger", LoggerService);
	LoggerService.$inject = [];
	function LoggerService() {
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
		
		that.log = function() {
			var message = stringify.apply(this, arguments);
			that.items.push(message);
			if (that.consoleLogEnabled) {
				console.log(message);
			}
			var remove = that.items.length - that.maxLines;
			if (remove > 0) {
				that.items.splice(0, remove);
			}
		}
		// NOTE: Overrides the global function log.
		log = that.log;
	}
	
	LoggerCtrl.$inject = ["logger"];
	function LoggerCtrl(logger) {
		this.items = logger.items;
		this.isEmpty = logger.isEmpty;
	}
	module.component("log", { 
		template: "<div ng-hide='$ctrl.isEmpty()' ng-repeat='item in $ctrl.items'><pre ng-bind='item' /></div>",
		controller: LoggerCtrl
	});
	
/*
	angular.element(document).ready(function() {
		var injector = angular.element(document).injector();
		log = injector.get("logger").log;
	});
	*/
	return module;
});
