var module = angular.module("myapp1");

Controller.$inject = ["slavi-logger", "$timeout", "single-runner"];
function Controller(logger, $timeout, runner) {
	var count = 0;
	this.clicked = function() {
		var cc = ++count;
		logger.log("pushed ", cc);
		var rr = runner.get("test-run", function(message) {
			logger.log("RR ", message);
		});
		$timeout(function() {
			rr.invoke(cc);
		}, 3000);
	};
}

module.component("app", { 
	template: "<button ng-click='$ctrl.clicked()'>Push me</button>",
	controller: Controller
});
module.run(["slavi-logger", function(logger) {
	logger.log('hi');
}]);
