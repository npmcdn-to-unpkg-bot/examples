var module = angular.module('myapp5');

Factory.$inject = ["$compile", "$sce"];
function Factory($compile, $sce) {
	
	Implementation.$inject = ["$scope", "$routeParams", "$resource", "$location", "$timeout", "myRouteService"];
	function Implementation($scope, $routeParams, $resource, $location, $timeout, myRouteService) {
		var that = this;

		function init() {
			that.mode = 'ERROR';
			that.formMeta = myRouteService.getMeta($routeParams.form);
			if (!that.formMeta) {
				that.msg = "No meta data for form " + $routeParams.form;
				return;
			}
			
			var resource = $resource(that.formMeta.baseUrl, {
//				callback: "JSON_CALLBACK",
					search: "",
					page: 0,
					size: 10,
					order: that.order
				}, {
					search: { method: "get" },
//				delete: { method: "delete", params: { id: "@id" }},
				load: { method: "get", params: { id: "@id" }},
//				new: { method: "get", params: { id: "new" }},
//				save: { method: "post", callback: "" }
				}, {
					stripTrailingSlashes: false,
					cancellable: true
				});

			var id = $routeParams.id;
			if (id) {
				id = id.split("/");
				angular.forEach(id, function(value, key) {
					id[key] = decodeURIComponent(value);
				});
				
				if (id.length !== that.formMeta.bestRowIdColumns.length) {
					that.msg = "Invalid ID";
					return;
				}
				
				that.selectedItem = null;

				var r = resource.load({ id: id });
				r.$promise.then(function() {
					that.selectedItem = r;
				});
				
				that.msg = "Id " + id;
				that.mode = 'EDIT';
			} else {
				that.msg = "No id";
				that.mode = 'LIST';
			}
		}
		
		init();
	}

	function link($scope, $element, $attrs, $ctrl, $transclude) {
		$scope.$watch("$ctrl.mode", function(mode) {
			var r = "";
			r += "<pre>{{$ctrl.msg}}</pre>";
			if (mode) {
				switch (mode) {
				case "LIST":
					r += '<my-panel data-title="{{ $ctrl.formMeta.label }}">';
					r += '<my-form-list-gen data-form-meta="$ctrl.formMeta" />';
					break;
				case "EDIT":
					r += '<my-panel ng-show="$ctrl.selectedItem" data-title="{{ $ctrl.formMeta.label }} - Details">';
					r += '<my-form-gen data-save="Done" data-item="$ctrl.selectedItem" data-form-meta="$ctrl.formMeta" />';
					break;
				case "ERROR": /* falls through */
				default:
					r += "<pre>{{$ctrl.msg}}</pre>";
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
		controller: Implementation,
		link: link
	};
}

module.directive("myRoute", Factory);
