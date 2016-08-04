var module = angular.module('my');

Implementation.$inject = ["$scope", "$q", "slavi-utils"];
function Implementation($scope, $q, utils) {
	var that = this;
	that.modelValue = "";
	
	function select(key) {
		var found = false;
		angular.forEach(that.itemList, function(item) {
			if (key === item.key) {
				found = true;
				that.model = item.key;
				that.modelValue = item.value;
			}
		});
		if (!found && key) {
			if (that.itemList) {
				that.model = that.itemList[0].key;
				that.modelValue = that.itemList[0].value;
			} else {
				that.model = undefined;
				that.modelValue = "";
			}
		}
	}
	
	$scope.$watchCollection("$ctrl.items", function(newValue) {
		that.itemList = utils.toKeyValueList(newValue);
		select(that.model);
	});

	var isInitialized = false;
	$scope.$watch("$ctrl.model", function(newValue) {
		if (!that.model)
			return;
		select(newValue);
		if (isInitialized) {
			that.onUpdate({ key: that.model, value: that.modelValue });
		}
		isInitialized = true;
	});
}

module.component('myCombo', {
	templateUrl: "my/myCombo.html",
	bindings: {
		items: "<",
		model: "=",
		onUpdate: "&"
	},
	controller: Implementation
});
