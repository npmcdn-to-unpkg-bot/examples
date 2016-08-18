var module = angular.module('myapp5');

Implementation.$inject = ["$q", "$resource", "slavi-logger"];
function Implementation($q, $resource, logger) {
	var that = this;
	
	var formMeta = [
		{
			label: "Locations",
			name: "locations",
			baseUrl: "api/locations/:id",
			bestRowIdColumns: ["id"],
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
					list_col_width: 5
				},
				{
					label: "e-mail",
					name: "email",
					type: "email",
					list_col_width: 5,
				},
				{
					label: "type",
					name: "type",
					type: "radio",
					values: [ "Unknown", "Headquarter", "Office", "Home" ],
					list_col_width: 0,
				},
				{
					label: "type (same with combo)",
					name: "type",
					type: "combo",
					values: [ "Unknown", "Headquarter", "Office", "Home" ]
				}
			]
		}
	];
	
	function fixSingleFormMeta(formMeta) {
		var i, totalWidth = 0;

		var dummy = angular.extend(
			{
				name: "",
				baseUrl: "",
				bestRowIdColumns: [],
				fields: []
			},
			formMeta
		);
		angular.extend(formMeta, dummy);
		
		formMeta.calcFields = [];
		formMeta.columnFields = [];
		formMeta.orderBy = [];

		if (formMeta && formMeta.fields) {
			for (i = 0; i < formMeta.fields.length; i++) {
				var tmp = angular.extend(
						{ name: i+1, label: "", list_col_width: 0, sortable: true },
						formMeta.fields[i]
				);
				if (tmp.label === null || tmp.label === "" || tmp.list_col_width === 0)
					tmp.sortable = false;
				formMeta.calcFields.push(tmp);
				totalWidth += tmp.list_col_width;
				if (tmp.sortable) {
					formMeta.columnFields.push(tmp);
					formMeta.orderBy.push({ k: "+" + tmp.name, v: tmp.label + " ascending" });
					formMeta.orderBy.push({ k: "-" + tmp.name, v: tmp.label + " descending" });
				}
			}
		}
		console.log("formMeta", formMeta);
		formMeta.order = formMeta.orderBy.length > 0 ? formMeta.orderBy[0].k : "";

		if (totalWidth < 1)
			totalWidth = formMeta.calcFields.length;
		if (totalWidth > 12)
			totalWidth = 12;

		var calcTotalWidth = 0;
		for (i = 0; i < formMeta.calcFields.length; i++) {
			var col = formMeta.calcFields[i];
			var c = col.list_col_width;
			c = Math.floor(c * 12 / totalWidth);
			if (c < 1)
				c = 1;
			if (c > 12)
				c = 12;
			calcTotalWidth += c;
			col._calcWidth = c;
		}
		for (i = formMeta.calcFields.length - 1; i >= 0; i--) {
			formMeta.calcFields[i]._calcWidth++;
			calcTotalWidth++;
			if (calcTotalWidth >= 12)
				break;
		}
	}
	
	
	that.formMeta = {};
	angular.forEach(formMeta, function(item) {
		fixSingleFormMeta(item);
		that.formMeta[item.name] = item;
	});
	
	that.getMeta = function(name) {
		return that.formMeta[name];
	};
}

module.service('myRouteService', Implementation);
