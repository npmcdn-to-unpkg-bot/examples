var module = angular.module('my');

Implementation.$inject = ["$scope", "$q", "slavi-utils"];
function Implementation($scope, $q, utils) {
	var that = this;
	that.modelValue = "";
	
	var isInitialized = false;
	function select(key) {
		var found = false;
		var model = null;
		var modelValue = null;
		
		angular.forEach(that.itemList, function(item) {
			if ((!found) && (key === item.key)) {
				found = true;
				model = item.key;
				modelValue = item.value;
			}
		});
		if (!found && key) {
			if (that.itemList) {
				model = that.itemList[0].key;
				modelValue = that.itemList[0].value;
			} else {
				model = undefined;
				modelValue = "";
			}
		}
		
		if (isInitialized && (
				(that.model !== model) ||
				(that.modelValue !== modelValue))
			) {
			that.form.$setDirty();
		}
		that.model = model;
		that.modelValue = modelValue;
	}
	
	$scope.$watchCollection("$ctrl.items", function(newValue) {
		that.itemList = utils.toKeyValueList(newValue);
		select(that.model);
	});

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

module.component('myRadio', {
	templateUrl: "my/myRadio.html",
	require: { 
		form: "^form"
	},
	bindings: {
		items: "<",
		model: "=",
		onUpdate: "&"
	},
	controller: Implementation
});
