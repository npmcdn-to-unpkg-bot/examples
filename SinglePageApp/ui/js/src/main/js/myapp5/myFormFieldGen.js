var module = angular.module('myapp5');

Factory.$inject = ["slavi-logger", "$timeout", "$sce"];
function Factory(logger, $timeout, $sce) {
	
	Instance.$inject = ["$scope"];
	function Instance($scope) {
		var that = this;
		that.html = "";

		console.log("my-form-field-gen created");
/*		
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
		};*/
	}
	
	function link($scope, $element, $attrs, ctrl, $transclude) {
		// ctrl.template
		console.log("inside link");
		ctrl.template = $sce.trustAsHtml("<pre>WTF?</pre>");
		var clone = $transclude($scope, function(clone) {
			console.log("some where inside transclude");
		});
	}

	function compile(element, attr) {
		console.log("inside compile");
		dd.template = $sce.trustAsHtml("<p>waiting even more...</p>");
		return link;
	}
	
	var dd = {
		//template: "<p>waiting...</p>",
		transclude: true,
		restrict: 'E',
		scope: false,
		controller: Instance,
		compile: compile
	};
	return dd;
}

module.directive("myFormFieldGen", Factory);

/*
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
	template: "<span ng-bind-html='$ctrl.html' ></span>",
	bindings: {
		item: "=",
		formFieldMeta: "="
//		save: "&"
	},
	controller: Implementation
});
*/