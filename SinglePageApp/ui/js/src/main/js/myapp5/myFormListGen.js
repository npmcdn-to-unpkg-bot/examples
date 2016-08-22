var module = angular.module('myapp5');

Implementation.$inject = ["$resource", "$routeParams", "$location", "slavi-utils", "myRouteService"];
function Implementation($resource, $routeParams, $location, utils, myRouteService) {
	var that = this;
	var delayedRunner = utils.delayedRunner();
	
	that.isDone = function() {
		return delayedRunner.isDone();
	};
	
	that.query = "";
	that.page = 1;
	that.size = 5;
	that.data = {};
	that.order = "";
	
	var resource = null;
	var selectedItem = null;
	
	that.updateListImediately = function() {
		selectedItem = null;
		if (!resource)
			return;
		var r = resource.search({ search: that.query, page: that.page-1, size: that.size, order: that.order });
		r.$promise.then(function() {
			that.data = r;
		});
	};

	that.updateListDelayed = function() {
		delayedRunner.run(that.updateListImediately);
	};
	
	that.isSelected = function(item) {
		return (item !== null) && (item === selectedItem);
	};
	
	that.onDblClick = function(item) {
		selectedItem = item;
		var url = myRouteService.getItemUrl(that.formMeta, item);
		$location.path(url);
	};
	
	that.onClick = function(item) {
		// toggle selected item
		// selectedItem = item === selectedItem ? null : item;
		if ((item !== null) && (item === selectedItem)) {
			that.onDblClick(item);
		} else {
			selectedItem = item;
		}
	};
	
	that.$onChanges = function() {
		resource = null;

		if (that.formMeta) {
			resource = $resource(that.formMeta.baseUrl, {
//			callback: "JSON_CALLBACK",
				search: "",
				page: 0,
				size: 10,
				order: that.order
			}, {
				search: { method: "get" },
//			delete: { method: "delete", params: { id: "@id" }},
//			load: { method: "get", params: { id: "@id" }},
//			new: { method: "get", params: { id: "new" }},
//			save: { method: "post", callback: "" }
			}, {
				stripTrailingSlashes: false,
				cancellable: true
			});
		}
		
		that.updateListImediately();
	};
	
	that.getHeaderColumnClass = function(index) {
		if (!that.formMeta)
			return "";
		var column = that.formMeta.calcFields[index];
		if ((!column) || (!column.sortable))
			return "";
		return "clickable col-sm-" + column.list_col_width; //_calcWidth;
	};
	
	that.doOrderBy = function(index) {
		if (!that.formMeta)
			return "";
		var column = that.formMeta.calcFields[index];
		if ((!column) || (!column.sortable))
			return;
		if (that.order === '+' + column.name) {
			that.order = '-' + column.name;
		} else {
			that.order = '+' + column.name;
		}
		that.updateListImediately();
	};

	that.getOrderByClass = function(index) {
		if (!that.formMeta)
			return "";
		var column = that.formMeta.calcFields[index];
		if ((!column) || (!column.sortable))
			return "";
		if (that.order === '+' + column.name) {
			return "glyphicon glyphicon-chevron-up";
		} else if (that.order === '-' + column.name) {
			return "glyphicon glyphicon-chevron-down";
		} else {
			return "";
		}
	};
}

module.component("myFormListGen", {
	templateUrl: "myapp5/myFormListGen.html",
	bindings: {
		formMeta: "<",
//		onSelect: "&"
	},
	scope: true,
	controller: Implementation
});
