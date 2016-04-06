define("slavi-log", ["jquery"], function($) {
	return {
		uiLogItemsMax: 10,
		uiLogEnabled: true,
		uiLogSelector: "#output",
		uiLogTag: "<pre>",
		consoleLogEnabled: true,

		log: function(message) {
			var uilog = $(uiLogSelector);
			if (!((settings.uiLogEnabled && !uilog.empty()) || settings.consoleLogEnabled))
				return;
			if (typeof message !== "string")
				message = JSON.stringify(message);
			if (settings.uiLogEnabled && !uilog.empty()) {
				$(uiLogTag).text(message).prependTo(uilog);
				var count = uilog.children().length;
				while (count-- > settings.uiLogItemsMax) {
					uilog.children().remove(":last-child");
				}
				uilog.scrollTop( 0 );
			}
			if (settings.consoleLogEnabled) {
				console.log(message);
			}
		},
		
		logClear: function() {
			$(uiLogSelector).children().remove();
		},
		
	};
});
