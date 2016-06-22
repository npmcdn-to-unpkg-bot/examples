var module = angular.module("myapp2");

Controller.$inject = ['$interval', 'dateFilter', "slavi-logger"];
function Controller($interval, dateFilter, logger) {
	function link(scope, element, attrs) {
		// start the UI update process; save the timeoutId for canceling
		var timeoutId = $interval(function() {
			updateTime(); // update DOM
		}, 1000);

		element.on('$destroy', function() {
			$interval.cancel(timeoutId);
		});

		var format;
		function updateTime() {
			element.text(dateFilter(new Date(), format));
		}

		scope.$watch(attrs.myCurrentTime, function(value) {
			format = value;
			updateTime();
		});
	}

	return {
		link: link
	};
}

module.directive("myCurrentTime", Controller);
