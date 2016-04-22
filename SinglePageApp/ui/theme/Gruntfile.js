module.exports = function( grunt ) {
	"use strict";

	grunt.initConfig( {
		params: {
			src: "src/main",
			out: "target",
			tmp: "<%= params.out %>/generated-resources",
			dest: "<%= params.out %>/classes",

			srcSprite: "<%= params.src %>/sprite",
			srcLess: "<%= params.src %>/less",
		},
		sprite:{
			all: {
				src: [
					"<%= params.srcSprite %>/**/*.png",
					"<%= params.srcSprite %>/**/*.bmp",
					"<%= params.srcSprite %>/**/*.gif",
					"<%= params.srcSprite %>/**/*.jpg",
				],
				dest: "<%= params.dest %>/sprite.png",
				imgPath: "sprite.png",
				cssFormat: "less",
				cssOpts: {
					//variableNameTransforms: ['camelize']
				},
				destCss: "<%= params.tmp %>/sprite.less"
			}
		},
		less: {
			all: {
				options: {
					paths: ["<%= params.srcLess %>", "<%= params.tmp %>"],
					compress: false,
					strictImports: true,
					banner: "// Built on <%= now %>\n",
				},
				files: {
					"<%= params.dest %>/style.css": "<%= params.srcLess %>/style.less"
				}
			}
		},
		cssmin: {
			options: {
				banner: "// Built on <%= now %>",
			},
			all: {
				files: [{
					expand: true,
					cwd: "<%= params.dest %>",
					src: ["**/*.css", "!**/*.min.css"],
					dest: "<%= params.dest %>/",
					ext: ".min.css",
					extDot: "last"
				}]
			}
		}
	} );

	var moment = require("moment");
	grunt.config.set("now", moment().format("YYYY-MM-DD hh:mm"));
	grunt.registerTask("all", ["sprite", "less", "cssmin"]);
	grunt.registerTask("default", ["all"]);
	require("load-grunt-tasks")(grunt);
};
