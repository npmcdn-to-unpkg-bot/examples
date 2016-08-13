var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$resource", "$routeParams", "$q", "$timeout", "locationService", "$parse", "slavi-logger", "slavi-utils"];
function Implementation($scope, $resource, $routeParams, $q, $timeout, service, $parse, logger, utils) {
	var that = this;
	that.service = service;
	$scope.service = service;
	var delayedRunner = utils.delayedRunner();
	
	that.isDone = function() {
		return delayedRunner.isDone();
	};
	
	that.query = "";
	that.page = 1;
	that.size = 5;
	that.data = {};
	that.orderBy = [];
	that.order = "";
	that.calcFields = [];
	
	that.updateList = function() {
		delayedRunner.run(function() {
			service.selectedItem = null;
			var r = service.resource.search({ search: that.query, page: that.page-1, size: that.size, order: that.order });
			r.$promise.then(function() {
				that.data = r;
			});
		});
	};
	
	that.onSelect = function(item) {
		// toggle selected item
		service.selectedItem = item === service.selectedItem ? null : item;
	};
	
	that.$onChanges = function() {
		var i, totalWidth = 0;
		that.calcFields = [];
		that.columnFields = [];
		that.orderBy = [];
		console.log("$onChanges ", that.formMeta);

		if (that.formMeta && that.formMeta.fields) {
			for (i = 0; i < that.formMeta.fields.length; i++) {
				var tmp = angular.extend(
						{ name: i+1, label: "", list_col_width: 0, sortable: true },
						that.formMeta.fields[i]
				);
				if (tmp.label === null || tmp.label === "" || tmp.list_col_width === 0)
					tmp.sortable = false;
				that.calcFields.push(tmp);
				totalWidth += tmp.list_col_width;
				if (tmp.sortable) {
					that.columnFields.push(tmp);
					that.orderBy.push({ k: "+" + tmp.name, v: tmp.label + " ascending" });
					that.orderBy.push({ k: "-" + tmp.name, v: tmp.label + " descending" });
				}
			}
		}
		that.order = that.orderBy ? that.orderBy[0].k : "";

		if (totalWidth < 1)
			totalWidth = that.calcFields.length;
		if (totalWidth > 12)
			totalWidth = 12;

		var calcTotalWidth = 0;
		for (i = 0; i < that.calcFields.length; i++) {
			var col = that.calcFields[i];
			var c = col.list_col_width;
			c = Math.floor(c * 12 / totalWidth);
			if (c < 1)
				c = 1;
			if (c > 12)
				c = 12;
			calcTotalWidth += c;
			col._calcWidth = c;
		}
		for (i = that.calcFields.length - 1; i >= 0; i--) {
			that.calcFields[i]._calcWidth++;
			calcTotalWidth++;
			if (calcTotalWidth >= 12)
				break;
		}
		that.updateList();
	};
	
	that.getHeaderColumnClass = function(index) {
		var column = that.calcFields[index];
		if ((!column) || (!column.sortable))
			return "";
		return "clickable col-sm-" + column.list_col_width; //_calcWidth;
	};
	
	that.doOrderBy = function(index) {
		var column = that.calcFields[index];
		if ((!column) || (!column.sortable))
			return;
		if (that.order === '+' + column.name) {
			that.order = '-' + column.name;
		} else {
			that.order = '+' + column.name;
		}
		that.updateList();
	};

	that.getOrderByClass = function(index) {
		var column = that.calcFields[index];
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
	},
	controller: Implementation
});
