var module = angular.module('myapp4.dialog');

Service.$inject = ["$q", "slavi-logger"];
function Service($q, logger) {
	this.confirm = function(message) {
		return $q.when(window.confirm(message || 'Is it OK?'));
	};
}

module.service('dialogService', Service);
