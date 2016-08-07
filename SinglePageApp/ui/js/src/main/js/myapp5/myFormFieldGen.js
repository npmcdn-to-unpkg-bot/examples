var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "slavi-logger", "$timeout"];
function Implementation($scope, logger, $timeout) {
	var that = this;

	that.html = "";

	that.$onChanges = function() {
		logger.log("onChange");

		switch (that.formFieldMeta.type) {
		case "radio":
			that.html = "<my-radio data-model='$ctrl.item[$ctrl.formFieldMeta.name]' data-items='$ctrl.formFieldMeta.values'></my-radio>";
			break;
		case "combo":
			that.html = "<my-combo data-model='$ctrl.item[$ctrl.formFieldMeta.name]' data-items='$ctrl.formFieldMeta.values'></my-combo>";
			break;
		case "label":
			that.html = "<p class='form-control-static'>{{$ctrl.item[$ctrl.formFieldMeta.name]}}</p>";
			break;
		case "email":
			that.html = "<input type='email' class='form-control' name='email' ng-model='$ctrl.item[$ctrl.formFieldMeta.name]' />";
			break;
		// case "text":
		default:
			that.html = "<input type='text' class='form-control' name='name' ng-model='$ctrl.item[$ctrl.formFieldMeta.name]' \
				ng-minlength='$ctrl.formFieldMeta.minLength' \
				ng-maxlength='$ctrl.formFieldMeta.maxLength' \
				ng-required='$ctrl.formFieldMeta.required' \
				ng-trim='$ctrl.formFieldMeta.trim' \
				ng-pattern='$ctrl.formFieldMeta.pattern'/>";
			break;
		}
	};
}

module.component('myFormFieldGen', {
	template: "<span ng-bind-template='$ctrl.html' ></span>",
	bindings: {
		item: "=",
		formFieldMeta: "="
//		save: "&"
	},
	controller: Implementation
});
