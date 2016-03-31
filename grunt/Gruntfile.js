module.exports = function( grunt ) {
	"use strict";

	var moment = require("moment");
	
	grunt.initConfig( {
		pkg: grunt.file.readJSON('package.json'),
		dirs: {
			src: "src/js",
			dest: "target",
			
		},
		requirejs: {
			compile: {
				options: {
					baseUrl: "src/js",
					name: "b",
					optimize: "none",
					include: [ "requireLib" ],
					paths: {
						requireLib: "../../node_modules/requirejs/require"
					},
					out: "target/js/main.js",
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
				src: ["<%= dirs.src %>/**.js"],
				dest: "<%= dirs.dest %>/all.js"
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
					cwd: "<%= dirs.dest %>",
					src: ["**    /*.js", "!**    /*.min.js"],
					dest: "<%= dirs.dest %>/",
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
	grunt.registerTask("all", ["concat", "uglify"]);
	grunt.registerTask("default", ["requirejs"]);
};
