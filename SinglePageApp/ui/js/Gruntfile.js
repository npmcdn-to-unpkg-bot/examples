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
					process: function(src, filepath) {
						return "\n// File " + filepath + "\n" + src;
					},
					banner: "// Built on <%= now %>\n",
				},
				src: [
					"<%= params.nodemodules %>/angular/angular.js",
					"<%= params.nodemodules %>/angular-route/angular-route.js",
					"<%= params.nodemodules %>/angular-resource/angular-resource.js",
					"<%= params.nodemodules %>/angular-animate/angular-animate.js",
					"<%= params.nodemodules %>/angular-touch/angular-touch.js",
					"<%= params.nodemodules %>/angular-cookies/angular-cookies.js",
					"<%= params.nodemodules %>/angular-sanitize/angular-sanitize.js",
					"<%= params.nodemodules %>/angular-translate/dist/angular-translate.js",
					"<%= params.nodemodules %>/angular-translate/dist/angular-translate-storage-cookie/angular-translate-storage-cookie.js",
					"<%= params.nodemodules %>/angular-translate/dist/angular-translate-storage-local/angular-translate-storage-local.js",
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
					"<%= params.srcjs %>/**/*.js",
					"!<%= params.srcjs %>/main.js",
					"<%= params.tmp %>/app-templates.js",
					"<%= params.tmp %>/i18n/messages_*.js",
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
		propertiesToJSON: {
			i18n: {
				src: ["<%= params.src %>/i18n/messages_*.properties"],
				dest: "<%= params.tmp %>/i18n",
				options: {
					______splitKeysBy: '.'
				}
			}
		},
		i18nextract: {
			default_options: {
				src: ['<%= params.srcjs %>/**/*.js', '<%= params.srcjs %>/**/*.html'],
				namespace: false,
				defaultLang: "en",
				lang: ["en", "bg"],
				prefix: "messages_",
				sufix: ".json",
				dest: "<%= params.tmp %>/i18n",
				safeMode: true
			}
		},
		jsonAngularTranslate: {
			i18n: {
				options: {
					moduleName: 'translations',
					extractLanguage: /..(?=\.[^.]*$)/,
					createNestedKeys: false
				},
				files: [{
					expand: true,
					cwd: '<%= params.tmp %>/i18n',
					src: 'messages_*.json',
					dest: '<%= params.tmp %>/i18n',
					ext: '.js'
				}]
			}
		},
		newer: {
			ngtemplates_app: {
				src: ["<%= params.srcjs %>/**/*.html"],
				dest: "<%= params.tmp %>/app-templates.js",
				options: { tasks: ["ngtemplates:app"] }
			},
			translations: {
				options: { tasks: ["translations"] }
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
	config.newer.translations.src	= config.propertiesToJSON.i18n.src;
	config.newer.translations.dest	= config.concat.app.dest;

	grunt.initConfig(config);
	var moment = require("moment");
	grunt.config.set("now", moment().format("YYYY-MM-DD HH:mm"));
	grunt.registerTask("translations", ["propertiesToJSON:i18n", "i18nextract", "jsonAngularTranslate:i18n"]);
	grunt.registerTask("all", ["jshint", "ngtemplates", "translations", "concat", "uglify"]);
	grunt.registerTask("default", ["newer"]);

	require("load-grunt-tasks")(grunt);
};
