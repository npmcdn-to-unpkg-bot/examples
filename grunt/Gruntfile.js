module.exports = function( grunt ) {
	"use strict";

	var moment = require("moment");
	
	grunt.initConfig( {
		pkg: grunt.file.readJSON('package.json'),
		dirs: {
			src: "src/main",
			dest: "target/webapp",

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
					name: "b",
					optimize: "none",
					include: [ "requirejs" ],
					paths: {
						requirejs:		"<%= dirs.nodemodules %>/requirejs/require",
						moment: 		"<%= dirs.nodemodules %>/moment/moment",
						jquery: 		"<%= dirs.nodemodules %>/jquery/dist/jquery",
					},
					out: "<%= dirs.destjs %>/main.js",
				}
			}
		},
		less: {
			all: {
				paths: "<%= dirs.srcless %>",
				compress: false,
				banner: "// Built on <%= now %>\n",
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
	grunt.registerTask("all", ["requirejs", "less"]);
	grunt.registerTask("default", ["all"]);
};
