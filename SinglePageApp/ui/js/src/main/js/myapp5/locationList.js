var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$resource", "$routeParams", "$q", "$timeout", "locationService", "$parse", "slavi-logger", "slavi-utils"];
function Implementation($scope, $resource, $routeParams, $q, $timeout, service, $parse, logger, utils) {
	var that = this;
	that.service = service;
	$scope.service = service;
	that.delayedRunner = utils.delayedRunner();
	
	that.isDone = function() {
		return that.delayedRunner.isDone();
	};
	
	that.invokeMe = function() {
		logger.log("invokeMe 1");
		
		var def = $q.defer();
		
		var p = $timeout(function() {
			return 0; //"some value";
		}, 1000);
		$q.when(p, function(v) {
			logger.log("--- Success ", v);
			if (!v) {
				def.reject();
			}
			def.resolve();
		}, function(v) {
			logger.log("--- Error ", v);
			def.reject();
		});
		logger.log("invokeMe 2");
		
		def.promise.then(function(v) {
			logger.log("Success ", v);
		}, function(v) {
			logger.log("Failure ", v);
		});
		logger.log("invokeMe 3");
	};
	
	that.onClick = function() {
		logger.log("onClick");
		var fn = $parse("$ctrl.invokeMe()");
		fn($scope, {});
	};
	
	that.query = "";
	that.page = 1;
	that.size = 5;
	that.data = {};
	that.orderBy = [
		{ k: "+name",		v: "Name asc" },
		{ k: "-name",		v: "Name desc" },
		{ k: "+id",			v: "ID asc" },
		{ k: "-id",			v: "ID desc" },
	];
	that.order = that.orderBy[0].k;
	
	that.updateList = function() {
		that.delayedRunner.run(function() {
			service.selectedItem = null;
			var r = service.resource.search({ search: that.query, page: that.page-1, size: that.size, order: that.order });
			r.$promise.then(function() {
				that.data = r;
			});
		});
	};
	that.updateList();
	
	that.onSelect = function(item) {
		// toggle selected item
		service.selectedItem = item === service.selectedItem ? null : item;
	};
	
	////////////////////// Table
	
	that.columns = [
		{ id: "id", name: "ID", width: 2 },
		{ id: "name", name: "Location", width: 3 },
	];

	that.calcColumns = [];
	
	$scope.$watchCollection("$ctrl.columns", function(newValue) {
		var i, totalWidth = 0;
		that.calcColumns = [];

		for (i = 0; i < newValue.length; i++) {
			var tmp = angular.extend(
					{ id: i+1, name: "", width: 1, sortable: true },
					newValue[i]
			);
			if (tmp.name === null || tmp.name === "")
				tmp.sortable = false;
			that.calcColumns.push(tmp);
			totalWidth += tmp.width;
		}

		if (totalWidth < 1)
			totalWidth = that.calcColumns.length;
		if (totalWidth > 12)
			totalWidth = 12;

		var calcTotalWidth = 0;
		for (i = 0; i < that.calcColumns.length; i++) {
			var col = that.calcColumns[i];
			var c = col.width;
			c = Math.floor(c * 12 / totalWidth);
			if (c < 1)
				c = 1;
			if (c > 12)
				c = 12;
			calcTotalWidth += c;
			col._calcWidth = c;
		}
		for (i = that.calcColumns.length - 1; i >= 0; i--) {
			that.calcColumns[i]._calcWidth++;
			calcTotalWidth++;
			if (calcTotalWidth >= 12)
				break;
		}
	});
	
	that.getHeaderColumnClass = function(index) {
		var column = that.calcColumns[index];
		if ((!column) || (!column.sortable))
			return "";
		return "clickable col-sm-" + column._calcWidth;
	};
	
	that.doOrderBy = function(index) {
		var column = that.calcColumns[index];
		if ((!column) || (!column.sortable))
			return;
		if (that.order === '+' + column.id) {
			that.order = '-' + column.id;
		} else {
			that.order = '+' + column.id;
		}
		that.updateList();
	};

	that.getOrderByClass = function(index) {
		var column = that.calcColumns[index];
		if ((!column) || (!column.sortable))
			return "";
		if (that.order === '+' + column.id) {
			return "glyphicon glyphicon-chevron-up";
		} else if (that.order === '-' + column.id) {
			return "glyphicon glyphicon-chevron-down";
		} else {
			return "";
		}
	};
	
	that.formMeta = {
		fields: [
			{
				label: "ID:",
				name: "id",
				type: "label"
			},
			{
				label: "Name:",
				name: "name",
				type: "text",
				minLength: 3,
				maxLength: 15,
				required: true,
				trim: true,
				pattern: "[A-Za-z@0-9]+"
			},
			{
				label: "e-mail:",
				name: "email",
				type: "email"
			},
			{
				label: "type:",
				name: "type",
				type: "radio",
				values: [ "Unknown", "Headquarter", "Office", "Home" ]
			},
			{
				label: "type (same with combo):",
				name: "type",
				type: "combo",
				values: [ "Unknown", "Headquarter", "Office", "Home" ]
			}
		]
	};
}

module.component("locationList", {
	templateUrl: "myapp5/locationList.html",
	controller: Implementation
});
