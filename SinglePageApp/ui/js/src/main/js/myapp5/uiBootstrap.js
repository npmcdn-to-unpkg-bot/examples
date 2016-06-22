var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$sce", "$templateCache", "slavi-logger"];
function Implementation($scope, $sce, $templateCache, logger) {
	var that = this;
	$scope.logger = logger;
	$scope.oneAtATime = true;

	$scope.groups = [{
		title: 'Dynamic Group Header - 1',
		content: 'Dynamic Group Body - 1'
	}, {
		title: 'Dynamic Group Header - 2',
		content: 'Dynamic Group Body - 2'
	}];
	$scope.popoverUrl = "myapp5/uiBootstrap-popover.html";
	$scope.status = {
		isOpen: false,
		dirtyCount: 0,
		isDirty: function() {
			return $scope.status.dirtyCount !== 0;
		}
	};

	$scope.$watch("status.isOpen", function() {
		$scope.status.dirtyCount = 0;
	});

	$scope.$watchCollection("logger.items", function() {
		if (!$scope.status.isOpen) {
			$scope.status.dirtyCount++;
		}
	});

	that.messageCount = 0;
	that.onAddMessageClick = function() {
		logger.log("Log message ", ++that.messageCount, " ", $scope.status);
		// logger.log("HTML:", $scope.htmlPopover);
	};
}

module.component('uiBootstrap', {
	templateUrl: 'myapp5/uiBootstrap.html',
	controller: Implementation
});
