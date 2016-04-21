module.exports = function( grunt ) {
	"use strict";

	var moment = require("moment");
	
	grunt.initConfig( {
		pkg: grunt.file.readJSON('package.json'),
		dirs: {
			src: "src/main",
			out: "target",
			tmp: "<%= dirs.out %>/generated-resources",
			dest: "<%= dirs.out %>/m2e-wtp/web-resources",

			srcSprite: "<%= dirs.src %>/sprite",
			destSpriteLess: "<%= dirs.tmp %>",

			srcjs: "<%= dirs.src %>/js",
			destjs: "<%= dirs.dest %>/js",
			
			srcless: "<%= dirs.src %>/less",
			destless: "<%= dirs.dest %>/css",
			
			nodemodules: "../../../node_modules/"
		},
/*		bowerRequirejs: {
			all: {
				rjsConfig: '<%= dirs.dest %>/app_config.js',
				options: {
					baseUrl: "./",
					transitive: true,
					excludeDev: true
				}
			}
		},*/
		requirejs: {
			compile: {
				options: {
					baseUrl: "<%= dirs.srcjs %>",
					name: "main",
					optimize: "none",
					include: [ "requirejs" ],
					paths: {
						"requirejs":			"<%= dirs.nodemodules %>/requirejs/require",
						"moment":				"<%= dirs.nodemodules %>/moment/moment",
						"jquery":				"<%= dirs.nodemodules %>/jquery/dist/jquery",
						"datatables.net":		"<%= dirs.nodemodules %>/datatables.net/js/jquery.dataTables",
						"datatables.net-dt":	"<%= dirs.nodemodules %>/datatables.net-dt",

						"es6-shim":				"<%= dirs.nodemodules %>/es6-shim/es6-shim",
						"system-polyfills":		"<%= dirs.nodemodules %>/systemjs/dist/system-polyfills",
						"shims_for_IE":			"<%= dirs.nodemodules %>/angular2/es6/dev/src/testing/shims_for_IE",
						"angular2-polyfills":	"<%= dirs.nodemodules %>/angular2/bundles/angular2-polyfills",
						"system.src":			"<%= dirs.nodemodules %>/systemjs/dist/system.src",
						"Rx":					"<%= dirs.nodemodules %>/rxjs/bundles/Rx.umd",
						"angular2":				"<%= dirs.nodemodules %>/angular2/bundles/angular2-all.umd",

						"angular":				"<%= dirs.nodemodules %>/angular/angular",
						"angular-route":		"<%= dirs.nodemodules %>/angular-route/angular-route",
						"angular-resource":		"<%= dirs.nodemodules %>/angular-resource/angular-resource",
						"ng-grid":				"<%= dirs.nodemodules %>/ng-grid/build/ng-grid",
					},
					out: "<%= dirs.destjs %>/main.js",
				}
			}
		},
		sprite:{
			all: {
				src: [
					"<%= dirs.srcSprite %>/**/*.png",
					"<%= dirs.srcSprite %>/**/*.bmp",
					"<%= dirs.srcSprite %>/**/*.gif",
					"<%= dirs.srcSprite %>/**/*.jpg",
				],
				dest: "<%= dirs.dest %>/sprite.png",
				imgPath: "sprite.png",
				cssFormat: "less",
				cssOpts: {
					//variableNameTransforms: ['camelize']
				},
				destCss: "<%= dirs.destSpriteLess %>/sprite.less"
			}
		},
		less: {
			all: {
				options: {
					paths: ["<%= dirs.srcless %>", "<%= dirs.destSpriteLess %>"],
					compress: false,
					strictImports: true,
					banner: "// Built on <%= now %>\n",
				},
				files: {
					"<%= dirs.destless %>/style.css": "<%= dirs.srcless %>/style.less"
				}
			}
		}

/*		concat: {
			options: {
				banner: "// Built on <%= now %>\n",
				process: function(src, filepath) {
					return "// Source: " + filepath + "\n" + src;
					// + src.replace(/(^|\n)[ \t]*('use strict'|"use strict");?\s*    /g, '$1');
				},
			},
			all: {
				src: ["<%= dirs.srcjs %>/**.js"],
				dest: "<%= dirs.destjs %>/all.js"
			}
		},
		
		uglify: {
			options: {
				preserveComments: false,
				sourceMap: true,
				report: "min",
				banner: "// Built on <%= now %>",
			},
			all: {
				files: [{
					expand: true,
					cwd: "<%= dirs.destjs %>",
					src: ["**    /*.js", "!**    /*.min.js"],
					dest: "<%= dirs.destjs %>/",
					ext: ".min.js",
					extDot: "last"
				}]
			}
		} */
	} );

	// Load grunt tasks from NPM packages
	require("load-grunt-tasks")(grunt);

	grunt.config.set("now", moment().format("YYYY-MM-DD hh:mm:ss"));
//	grunt.registerTask("all", ["concat", "uglify"]);
	grunt.registerTask("all", ["requirejs", "sprite", "less"]);
	grunt.registerTask("default", ["all"]);
};
