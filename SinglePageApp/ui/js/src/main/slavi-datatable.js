define("slavi-datatable", ["jquery", "slavi-log"], function($, log) {
	var defaultDatatableParams = {
		selector: "#table",
		tableUrl: "filter.json",
		orderColumn: 1,
		orderDirection: "asc",
		editEnabled: true,
		editClass: "icon-view",
		deleteEnabled: true,
		deleteClass: "icon-delete",
	};
	
	return {
		dataTable: function(params, tableId, tableUrl) {
			$.extend(params, defaultDatatableParams);
			
			$(params.selector).dataTable({
				"processing" : true,
				"serverSide" : true,
				"ajax" : params.tableUrl,
				"order": [[params.orderColumn, params.orderDirection]],
				"columns": [
					{ "data": "id", 
						render: function(data, type, row, meta) {
							var r = "";
							if (params.editEnabled) {
								r += "<a class='" + params.editClass + "'" + "href='" + " "; // TODO: more...
							}
							return "<a class='icon-view' href='" + ws.url("tag/" + data + "/") + "' /><a class='icon-delete' href='" + ws.url("tag/" + data + "/deleteTag?tagId=" + data) + "' />";
						}
					},
					{ "data": "name" }
				]
			});
		},
		
	};
});
