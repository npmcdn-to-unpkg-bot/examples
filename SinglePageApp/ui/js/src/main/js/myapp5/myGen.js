var module = angular.module('myapp5');

Implementation.$inject = ["$scope", "$resource", "$routeParams", "$q", "$timeout", "$parse"];
function Implementation($scope, $resource, $routeParams, $q, $timeout, $parse) {
	var that = this;
	
	that.invokeMe = function() {
		console.log("invokeMe 1");
		
		var def = $q.defer();
		
		var p = $timeout(function() {
			return 0; //"some value";
		}, 1000);
		$q.when(p, function(v) {
			console.log("--- Success ", v);
			if (!v) {
				def.reject();
			}
			def.resolve();
		}, function(v) {
			console.log("--- Error ", v);
			def.reject();
		});
		console.log("invokeMe 2");
		
		def.promise.then(function(v) {
			console.log("Success ", v);
		}, function(v) {
			console.log("Failure ", v);
		});
		console.log("invokeMe 3");
	};
	
	that.onClick = function() {
		console.log("onClick");
		var fn = $parse("$ctrl.invokeMe()");
		fn($scope, {});
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
	
	that.formMeta = {
		fields: [
			{
				label: "ID",
				name: "id",
				type: "label",
				list_col_width: 2,
				//sortable: false
			},
			{
				label: "Name",
				name: "name",
				type: "text",
				minLength: 3,
				maxLength: 15,
				required: true,
				trim: true,
				pattern: "[A-Za-z@0-9]+",
				list_col_width: 3
			},
			{
				label: "e-mail",
				name: "email",
				type: "email"
			},
			{
				label: "type",
				name: "type",
				type: "radio",
				values: [ "Unknown", "Headquarter", "Office", "Home" ]
			},
			{
				label: "type (same with combo)",
				name: "type",
				type: "combo",
				values: [ "Unknown", "Headquarter", "Office", "Home" ]
			}
		]
	};
}

module.component("myGen", {
	templateUrl: "myapp5/myGen.html",
	controller: Implementation
});
