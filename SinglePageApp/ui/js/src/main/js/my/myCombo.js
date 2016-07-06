var module = angular.module('my');

Implementation.$inject = ["$scope", "$q"];
function Implementation($scope, $q) {
	var that = this;
	that.modelValue = "";
	
	$scope.$watchCollection("$ctrl.items", function(newValue) {
		console.log("collection ", that.model);
		that.itemList = [];
		var found = false;
		var isArray = newValue && Array === newValue.constructor; 
		angular.forEach(newValue, function(v, k) {
			if (isArray) {
				if (v instanceof Object) {
					k = v.k ? v.k :
						v.key ? v.key :
						v.id ? v.id :
						v.name ? v.name :
						k;
					v = v.v ? v.v :
						v.val ? v.val :
						v.value ? v.value :
						v.label ? v.label :
						v;
				} else {
					k = v;
				}
			}
			that.itemList.push({ v:v, k:k });
			if (that.model === k) {
				found = true;
				that.modelValue = v;
			}
		});
		if (!found && that.model) {
			if (that.itemList) {
				that.model = that.itemList[0].k;
				that.modelValue = that.itemList[0].v;
			} else {
				that.model = undefined;
				that.modelValue = "";
			}
		}
	});

	var isInitialized = false;
	$scope.$watch("$ctrl.model", function(newValue) {
		console.log("value ", that.model);
		if (!that.model)
			return;
		var found = false;
		angular.forEach(that.itemList, function(item) {
			if (newValue === item.k) {
				found = true;
				that.modelValue = item.v;
			}
		});
		if (!found) {
			if (that.itemList) {
				that.model = that.itemList[0].k;
				that.modelValue = that.itemList[0].v;
			} else {
				that.model = undefined;
				that.modelValue = "";
			}
		}
		if (isInitialized) {
			that.onUpdate({ key: that.model, value: that.modelValue });
		}
		isInitialized = true;
	});
}

module.component('myCombo', {
	templateUrl: "my/myCombo.html",
	transclude: true,
	bindings: {
		items: "<",
		model: "=",
		onUpdate: "&"
	},
	controller: Implementation
});
