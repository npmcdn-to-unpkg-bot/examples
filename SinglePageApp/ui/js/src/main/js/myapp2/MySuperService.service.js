var module = angular.module("myapp2");

function MyLauncher(myNum) {
	this.getMyNum = function() {
		return "My num is " + myNum;
	};
}

Service.$inject = [];
function Service() {
	var myNum = 5;
	
	this.setMyNum = function(_myNum) {
		myNum = _myNum;
	};
	
	this.$get = [function() {
		return new MyLauncher(myNum);
	}];
}

module.provider("MySuperService", Service);

module.config(["MySuperServiceProvider", function(myService) {
	myService.setMyNum("Data 2");
}]);
