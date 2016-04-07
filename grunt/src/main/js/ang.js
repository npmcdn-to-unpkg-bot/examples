define("dummy_ang_1", ["es6-shim"], function() { return {}; });
define("dummy_ang_2", ["dummy_ang_1", "system-polyfills"], function() { return {}; });
define("dummy_ang_3", ["dummy_ang_2", "shims_for_IE"], function() { return {}; });
define("dummy_ang_4", ["dummy_ang_3", "angular2-polyfills"], function() { return {}; });
define("dummy_ang_5", ["dummy_ang_4", "system.src"], function() { return {}; });
define("dummy_ang_6", ["dummy_ang_5", "Rx"], function() { return {}; });
define("dummy_ang_7", ["dummy_ang_6", "angular2"], function() { return {}; });

define("ang", ["jquery", "dummy_ang_7"], function($) {
	$(document).ready(function() {
		$("<pre>").text("Done.").appendTo($("#container"));

		System.config({
			packages: {
				app: {
					format: 'register',
					defaultExtension: 'js'
				}
			}
		});
		// System.import('app/main').then(null, console.error.bind(console));
	});

	return {
	};
});
