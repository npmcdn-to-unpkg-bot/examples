var module = angular.module("slavi-log");

function stringify1(obj) {
	if (typeof obj === "string")
		return obj;

	var seen = [];
	return JSON.stringify(obj, function(key, val) {
		if (val !== null && typeof val === "object") {
			if (seen.indexOf(val) >= 0) {
				return "... cyclic ref ...";
			}
			seen.push(val);
		}
		return val;
	});
}

function stringify() {
	var result = "";
	for (var i = 0; i < arguments.length; i++) {
		var obj = arguments[i];
		result = result + stringify1(obj);
	}
	return result;
}
	
module.service("slavi-logger", Service);
Service.$inject = [];
function Service() {
	var that = this;
	that.maxLines = 10;
	that.consoleLogEnabled = true;
	that.items = [];
	
	that.isEmpty = function() {
		return that.items.length === 0;
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
	};
}

