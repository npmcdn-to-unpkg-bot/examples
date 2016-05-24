/*
// Angular 2
define("dummy_ang_1", ["slavi-log", "es6-shim"], 				function(dummy, a) { dummy["es6-shim"] = a; return dummy; });
define("dummy_ang_2", ["dummy_ang_1", "system-polyfills"],		function(dummy, a) { dummy["system-properties"] = a; return dummy; });
define("dummy_ang_3", ["dummy_ang_2", "shims_for_IE"],			function(dummy, a) { dummy["shims_for_IE"] = a; return dummy; });
define("dummy_ang_4", ["dummy_ang_3", "angular2-polyfills"], 	function(dummy, a) { dummy["angular2-polyfills"] = a; return dummy; });
define("dummy_ang_5", ["dummy_ang_4", "system.src"], 			function(dummy, a) { dummy["system.src"] = a; return dummy; });
define("dummy_ang_6", ["dummy_ang_5", "Rx"], 					function(dummy, a) { dummy["Rx"] = a; return dummy; });
define("dummy_ang_7", ["dummy_ang_6", "angular2"], 				function(dummy, a) { dummy["angular2"] = a; return dummy; });

define("ang", ["jquery", "dummy_ang_7"], function($, dummy) {
	System.config({
		packages: {
			app: {
				format: 'register',
				defaultExtension: 'js'
			}
		}
	});
	// System.import('app/main').then(null, console.error.bind(console));

	return dummy;
});

*/

// Angular 1
define("dummy_ang_1", ["requirejs"],			 				function(r)        { return { "requirejs": r }; });
//define("dummy_ang_2", ["dummy_ang_1", "jquery"],				function(dummy, a) { dummy["jquery"] = a; return dummy; });
define("dummy_ang_3", ["dummy_ang_1", "angular"],				function(dummy, a) { dummy["angular"] = a; return dummy; });
define("dummy_ang_4", ["dummy_ang_3", "angular-route"],			function(dummy, a) { dummy["angular-route"] = a; return dummy; });
//define("dummy_ang_5", ["dummy_ang_4", "angular-resource"],		function(dummy, a) { dummy["angular-resource"] = a; return dummy; });
//define("dummy_ang_6", ["dummy_ang_5", "ng-grid"],				function(dummy, a) { dummy["ng-grid"] = a; return dummy; });
define("dummy_ang_7", ["dummy_ang_3", "slavi-log"],				function(dummy, a) { dummy["log"] = a; return dummy; });

define("ang", ["dummy_ang_7"], function(dummy) {
/*	System.config({
		packages: {
			app: {
				format: 'register',
				defaultExtension: 'js'
			}
		}
	});*/
	// System.import('app/main').then(null, console.error.bind(console));
	return dummy;
});
