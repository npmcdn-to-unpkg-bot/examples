var module = angular.module('myapp5');

Factory.$inject = ["slavi-logger", "$timeout", "$compile", "$sce"];
function Factory(logger, $timeout, $compile, $sce) {
	
	Instance.$inject = ["$scope"];
	function Instance($scope) {
		var that = this;

		$scope.$watch("$ctrl.formFieldMeta", function(value) {
			logger.log("onChange: formFieldGen ", value);

			
			if (!value)
				return;
			that.formFieldMeta = value;
			
			var r = "";
			switch (that.formFieldMeta.type) {
			case "radio":
				r = "<my-radio data-model='$ctrl.item[$ctrl.formFieldMeta.name]' data-items='$ctrl.formFieldMeta.values'></my-radio>";
				break;
			case "combo":
				r = "<my-combo data-model='$ctrl.item[$ctrl.formFieldMeta.name]' data-items='$ctrl.formFieldMeta.values'></my-combo>";
				break;
			case "label":
				r = "<p class='form-control-static'>{{$ctrl.item[$ctrl.formFieldMeta.name]}}</p>";
				break;
			case "email":
				r = "<input type='email' class='form-control' name='email' ng-model='$ctrl.item[$ctrl.formFieldMeta.name]' />";
				break;
			// case "text":
			default:
				r = "<input type='text' class='form-control' name='name' ng-model='$ctrl.item[$ctrl.formFieldMeta.name]' \
					ng-minlength='$ctrl.formFieldMeta.minLength' \
					ng-maxlength='$ctrl.formFieldMeta.maxLength' \
					ng-required='$ctrl.formFieldMeta.required' \
					ng-trim='$ctrl.formFieldMeta.trim' \
					ng-pattern='$ctrl.formFieldMeta.pattern'/>";
				break;
			}
			that.html = r;
			console.log("field gen: $ctrl.formFieldMeta watch ", that.html);
		});
	}

	function link($scope, $element, $attrs, ctrl, $transclude) {
		console.log("field gen: link");
		$scope.$watch("$ctrl.html", function(html) {
			console.log("field gen: watch ", html);
			$element.html($sce.trustAsHtml(html));
			$compile($element.contents())($scope);
		});
	}

	var dd = {
		//template: "<p>waiting...</p>",
		transclude: true,
		restrict: 'ECA',
		scope: {
			item: "=",
			formFieldMeta: "="
		},
		controller: Instance,
		controllerAs: "$ctrl",
		//controller: angular.noop,
		//compile: compile
		link: link
	};
	return dd;
}

module.directive("myFormFieldGen", Factory);
