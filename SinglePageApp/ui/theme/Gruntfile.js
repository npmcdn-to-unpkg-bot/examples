module.exports = function( grunt ) {
	"use strict";

	grunt.initConfig({
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
				dest: "<%= params.dest %>/image/sprite.png",
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
					paths: ["<%= params.srcLess %>", "<%= params.srcLess %>/include", "<%= params.tmp %>", "node_modules/bootstrap/less"],
					compress: false,
					strictImports: true,
					banner: "// Built on <%= now %>\n",
				},
				files: {
					"<%= params.dest %>/css/style.css": [
						// "node_modules/bootstrap/dist/css/bootstrap.css", 
						"<%= params.srcLess %>/**/*.less", 
						"!<%= params.srcLess %>/include/**/*.less"
					]
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
		},
		newer: {
			sprite: {
				src: [
					"<%= params.srcSprite %>/**/*.png",
					"<%= params.srcSprite %>/**/*.bmp",
					"<%= params.srcSprite %>/**/*.gif",
					"<%= params.srcSprite %>/**/*.jpg",
				],
				dest: "<%= params.tmp %>/sprite.less",
				options: { tasks: ["sprite"] }
			},
			less: {
				src: [
					"<%= params.srcLess %>/**/*.less",
					"<%= params.tmp %>/sprite.less"
				],
				dest: "<%= params.dest %>/css/style.css",
				options: { tasks: ["less"] }
			},
			cssmin: {
				src: ["<%= params.dest %>/css/style.css"],
				dest: "<%= params.dest %>/css/style.min.css",
				options: { tasks: ["cssmin"] }
			}
		}
	});

	var moment = require("moment");
	grunt.config.set("now", moment().format("YYYY-MM-DD HH:mm"));
	grunt.registerTask("all", ["sprite", "less", "cssmin"]);
	grunt.registerTask("default", ["newer"]);
	require("load-grunt-tasks")(grunt);
};
