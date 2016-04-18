define("slavi-log", ["jquery"], function($) {
	var settings = {
		uiLogItemsMax: 10,
		uiLogSelector: "#output",
		uiLogTag: "<pre>",
		uiLogEnabled: true,
		consoleLogEnabled: true,
	};

	function stringify(obj) {
		var seen = [];
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

	function log(message) {
		var uilog = $(settings.uiLogSelector);
		if (!((settings.uiLogEnabled && !uilog.empty()) || settings.consoleLogEnabled))
			return;
		if (typeof message !== "string")
			message = stringify(message);
		if (settings.uiLogEnabled && !uilog.empty()) {
			$(settings.uiLogTag).text(message).prependTo(uilog);
			var count = uilog.children().length;
			while (count-- > settings.uiLogItemsMax) {
				uilog.children().remove(":last-child");
			}
			uilog.scrollTop( 0 );
		}
		if (settings.consoleLogEnabled) {
			console.log(message);
		}
	};

	function logClear() {
		$(settings.uiLogSelector).children().remove();
	};
	
	return {
		stringify: stringify,
		log: log,
		logClear: logClear,
	};
});
