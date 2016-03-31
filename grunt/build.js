({
	baseUrl: "src/js",
	out: "target/js/main.js",
	name: "b",
	optimize: "none",
	include: "requireLib",
	paths: {
		requireLib: "../../node_modules/requirejs/require"
	}
})