module.exports = function( grunt ) {
	"use strict";

	grunt.initConfig( {
		params: {
			src: "src/main",
			out: "target",
			tmp: "<%= params.out %>/generated-resources",
			dest: "<%= params.out %>/classes",

			srcjs: "<%= params.src %>/js",
			nodemodules: "../../../node_modules/"
		},
		requirejs: {
			compile: {
				options: {
					baseUrl: "<%= params.srcjs %>",
					name: "main",
					optimize: "none",
					include: [ "requirejs" ],
					paths: {
						"requirejs":			"<%= params.nodemodules %>/requirejs/require",
						"moment":				"<%= params.nodemodules %>/moment/moment",
						"jquery":				"<%= params.nodemodules %>/jquery/dist/jquery",
						"datatables.net":		"<%= params.nodemodules %>/datatables.net/js/jquery.dataTables",
						"datatables.net-dt":	"<%= params.nodemodules %>/datatables.net-dt",

						"es6-shim":				"<%= params.nodemodules %>/es6-shim/es6-shim",
						"system-polyfills":		"<%= params.nodemodules %>/systemjs/dist/system-polyfills",
						"shims_for_IE":			"<%= params.nodemodules %>/angular2/es6/dev/src/testing/shims_for_IE",
						"angular2-polyfills":	"<%= params.nodemodules %>/angular2/bundles/angular2-polyfills",
						"system.src":			"<%= params.nodemodules %>/systemjs/dist/system.src",
						"Rx":					"<%= params.nodemodules %>/rxjs/bundles/Rx.umd",
						"angular2":				"<%= params.nodemodules %>/angular2/bundles/angular2-all.umd",

						"angular":				"<%= params.nodemodules %>/angular/angular",
						//"angular-route":		"<%= params.nodemodules %>/angular-route/angular-route",
						"angular-route":		"<%= params.nodemodules %>/@angular/router/angular1/angular_1_router",
						"angular-resource":		"<%= params.nodemodules %>/angular-resource/angular-resource",
						"ng-grid":				"<%= params.nodemodules %>/ng-grid/build/ng-grid",
					},
					out: "<%= params.dest %>/alljs.js",
				}
			}
		},
		uglify: {
			options: {
				preserveComments: false,
				sourceMap: false,
				report: "min",
				banner: "// Built on <%= now %>\n",
			},
			all: {
				files: [{
					expand: true,
					cwd: "<%= params.dest %>",
					src: ["*.js", "!*.min.js"],
					dest: "<%= params.dest %>/",
					ext: ".min.js",
					extDot: "last"
				}]
			}
		}
	} );

	var moment = require("moment");
	grunt.config.set("now", moment().format("YYYY-MM-DD hh:mm"));
	grunt.registerTask("all", ["requirejs", "uglify"]);
	grunt.registerTask("default", ["all"]);
	require("load-grunt-tasks")(grunt);
};
