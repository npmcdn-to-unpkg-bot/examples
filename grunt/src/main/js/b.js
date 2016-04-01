define("b", ["a"], function(a) {
	a.prototype.myfunc = function(x) {
		a.log("myfunc: " + x);
	};
	
	return {
		bbb: function() {
			a.myfunc("bbb")
		},
	};
});



