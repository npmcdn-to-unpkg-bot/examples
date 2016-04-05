define("a", ["jquery", "moment"], function($, m) {
	$(document).ready(function() {
		alert('hi 4');
		$("<pre>").val("Done.").appendTo($("#container"));
	});
	
	return {
		log: function(x) {
			console.log(x);
		}
	};
});
