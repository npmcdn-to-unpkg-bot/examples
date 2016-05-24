var module = angular.module("app");

var log = function() {
	console.log(arguments);
}

module.component("app", { 
	template: "<button ng-click='$ctrl.clicked()'>Push me</button>",
	controller: AppCtrl
});
AppCtrl.$inject = ["slavi-logger", "$timeout", "single-runner"];
function AppCtrl(logger, $timeout, runner) {
	var count = 0;
	this.clicked = function() {
		var cc = ++count;
		log("pushed ", cc);
		var rr = runner.get("test-run", function(message) {
			logger.log("RR ", message);
		});
		$timeout(function() {
			rr.invoke(cc)
		}, 3000);
	}
}

module.run(["slavi-logger", function(logger) {
	log = logger.log;
	log('hi');
}]);
