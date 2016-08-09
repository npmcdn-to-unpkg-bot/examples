var module = angular.module('myapp5');

Factory.$inject = ["$compile", "$sce"];
function Factory($compile, $sce) {
	
	Instance.$inject = [];
	function Instance() {
	}

	function link($scope, $element, $attrs, $ctrl, $transclude) {
		$scope.$watch("$ctrl.formFieldMeta.type", function(fieldType) {
			var r = "";
			if ($ctrl.formFieldMeta) {
				switch (fieldType) {
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
				case "text": /* falls through */
				default:
					r = "<input type='text' class='form-control' name='name' ng-model='$ctrl.item[$ctrl.formFieldMeta.name]'";
					if ($ctrl.formFieldMeta.minLength)
						r += " ng-minlength='$ctrl.formFieldMeta.minLength'";
					if ($ctrl.formFieldMeta.minLength)
						r += " ng-maxlength='$ctrl.formFieldMeta.maxLength'";
					if ($ctrl.formFieldMeta.required)
						r += " ng-required='$ctrl.formFieldMeta.required'";
					if ($ctrl.formFieldMeta.trim)
						r += " ng-trim='$ctrl.formFieldMeta.trim'";
					if ($ctrl.formFieldMeta.pattern)
						r += " ng-pattern='$ctrl.formFieldMeta.pattern'";
					r += " />";
					break;
				}
			}
			$element.html($sce.trustAsHtml(r));
			$compile($element.contents())($scope);
		});
	}

	return {
		restrict: "E",
		bindToController: true,
		scope: {
			item: "=",
			formFieldMeta: "="
		},
		controllerAs: "$ctrl",
		controller: Instance,
		link: link
	};
}

module.directive("myFormFieldGen", Factory);
