module.exports = function( grunt ) {
	"use strict";

	var config = {
		params: {
			src: "src/main",
			out: "target",
			tmp: "<%= params.out %>/generated-resources",
			dest: "<%= params.out %>/classes/js",

			srcjs: "<%= params.src %>/js",
			nodemodules: "node_modules/"
		},
/*		requirejs: { 
			compile: {
				options: {
					baseUrl: "<%= params.srcjs %>",
					name: "main",
					optimize: "none",
					include: [ "requirejs" ],
					paths: {
						"requirejs":			"<%= params.nodemodules %>/requirejs/require",
						//"moment":				"<%= params.nodemodules %>/moment/moment",
						"jquery":				"<%= params.nodemodules %>/jquery/dist/jquery",
						//"datatables.net":		"<%= params.nodemodules %>/datatables.net/js/jquery.dataTables",
						//"datatables.net-dt":	"<%= params.nodemodules %>/datatables.net-dt",

						//"es6-shim":				"<%= params.nodemodules %>/es6-shim/es6-shim",
						//"system-polyfills":		"<%= params.nodemodules %>/systemjs/dist/system-polyfills",
						//"shims_for_IE":			"<%= params.nodemodules %>/angular2/es6/dev/src/testing/shims_for_IE",
						//"angular2-polyfills":	"<%= params.nodemodules %>/angular2/bundles/angular2-polyfills",
						//"system.src":			"<%= params.nodemodules %>/systemjs/dist/system.src",
						//"Rx":					"<%= params.nodemodules %>/rxjs/bundles/Rx.umd",
						//"angular2":				"<%= params.nodemodules %>/angular2/bundles/angular2-all.umd",

						"angular":				"<%= params.nodemodules %>/angular/angular",
						//"angular-route":		"<%= params.nodemodules %>/angular-route/angular-route",
						"angular-route":		"<%= params.nodemodules %>/@angular/router/angular1/angular_1_router",
						"angular-resource":		"<%= params.nodemodules %>/angular-resource/angular-resource",
						//"ng-grid":				"<%= params.nodemodules %>/ng-grid/build/ng-grid",
					},
					useStrict: true,
					wrap: {
						start: "// Built on <%= now %>\n'use strict';\n",
						end: ""
					},
					out: "<%= params.dest %>/alljs.js",
				}
			}
		},*/
		jshint: {
			options: {
				multistr: true,
				force: false
			},
			app: ["<%= params.srcjs %>/**/*.js"]
		},
		ngtemplates: {
			options: {
				module: "templates",
				htmlmin: { collapseWhitespace: true, collapseBooleanAttributes: true }
			},
			app: {
				cwd: "<%= params.srcjs %>/",
				src: "**/*.html",
				dest: "<%= params.tmp %>/app-templates.js",
			}
		},
		concat: {
			libs: {
				options: {
					banner: "// Built on <%= now %>\n",
				},
				src: [
					"<%= params.nodemodules %>/angular/angular.js",
					"<%= params.nodemodules %>/angular-route/angular-route.js",
					"<%= params.nodemodules %>/angular-resource/angular-resource.js",
					"<%= params.nodemodules %>/angular-animate/angular-animate.js",
					"<%= params.nodemodules %>/angular-touch/angular-touch.js",
					"<%= params.nodemodules %>/angular-ui-bootstrap/dist/ui-bootstrap-tpls.js",
				],
				dest: "<%= params.dest %>/libs.js"
			},
			app: {
				options: {
					process: function(src, filepath) {
						return "// File " + filepath + "\n(function() {\n" + src + "\n})();\n";
					},
					banner: "// Built on <%= now %>\n'use strict';\n",
				},
				src: [
					"<%= params.srcjs %>/**/*.module.js",
					"<%= params.srcjs %>/**/*.service.js",
					"<%= params.srcjs %>/**/*.ctrl.js",
					"<%= params.srcjs %>/**/*.js",
					"!<%= params.srcjs %>/main.js",
					"<%= params.tmp %>/app-templates.js",
					"<%= params.srcjs %>/main.js",	// main.js goes last
				],
				dest: "<%= params.dest %>/app.js"
			}
		},
		uglify: {
			options: {
				preserveComments: false,
				sourceMap: false,
				report: "min",
				banner: "// Built on <%= now %>\n",
			},
			libs: {
				files: [{
					src: ["<%= params.dest %>/libs.js"],
					dest: "<%= params.dest %>/libs.min.js",
				}]
			},
			app: {
				files: [{
					src: ["<%= params.dest %>/app.js"],
					dest: "<%= params.dest %>/app.min.js",
				}]
			},
/*			all: {
				files: [{
					expand: true,
					cwd: "<%= params.dest %>",
					src: ["*.js", "!*.min.js"],
					dest: "<%= params.dest %>/",
					ext: ".min.js",
					extDot: "last"
				}]
			} */
		},
		newer: {
			ngtemplates_app: {
				src: ["<%= params.srcjs %>/**/*.html"],
				dest: "<%= params.tmp %>/app-templates.js",
				options: { tasks: ["ngtemplates:app"] }
			},
			concat_libs: {
				options: { tasks: ["concat:libs"] }
			},
			concat_app: {
				options: { tasks: ["jshint:app", "concat:app"] }
			},
			uglify_libs: {
				src: ["<%= params.dest %>/libs.js"],
				dest: "<%= params.dest %>/libs.min.js",
				options: { tasks: ["uglify:libs"] }
			},
			uglify_app: {
				src: ["<%= params.dest %>/app.js"],
				dest: "<%= params.dest %>/app.min.js",
				options: { tasks: ["uglify:app"] }
			}
		}
	};
	config.newer.concat_libs.src	= config.concat.libs.src;
	config.newer.concat_libs.dest	= config.concat.libs.dest;
	config.newer.concat_app.src		= config.concat.app.src;
	config.newer.concat_app.dest	= config.concat.app.dest;

	grunt.initConfig(config);
	var moment = require("moment");
	grunt.config.set("now", moment().format("YYYY-MM-DD HH:mm"));
	grunt.registerTask("all", ["jshint", "ngtemplates", "concat", "uglify"]);
	grunt.registerTask("default", ["newer"]);

	require("load-grunt-tasks")(grunt);
};
