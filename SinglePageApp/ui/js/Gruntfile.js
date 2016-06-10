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
			}
		},
		newer: {
			ngtemplates_app: {
				src: ["<%= params.srcjs %>/**/*.html"],
				dest: "<%= params.tmp %>/app-templates.js",
				options: { tasks: ["ngtemplates:app"] }
			},
			concat_libs: {
				options: { tasks: ["concat:libs", "uglify:libs"] }
			},
			concat_app: {
				options: { tasks: ["jshint:app", "concat:app", "uglify:app"] }
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
