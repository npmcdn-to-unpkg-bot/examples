// TODO: Nice tips at: http://www.bennadel.com/blog/2616-aborting-ajax-requests-using-http-and-angularjs.htm
var module = angular.module("slavi-utils");

var runners = {};

function create(name) {
	var generation = 0;
	var scheduled = null;
	var runner = {
		lastResult: null,
		lastError: null,
		
		request: function(theFunction) {
			var req = {
				arguments: [],
				_this: null,
			}
			req.generation = ++generation;
			req.theFunction = theFunction;
			
			function invoke() {
				if (req.generation != generation) {
					return;
				}
				req.arguments = arguments;
				req._this = this;
				if (scheduled != null) {
					scheduled = req;
					return;
				}
				
				scheduled = req;
				while (scheduled != null) {
					var curReq = scheduled;
					scheduled = null;
					try {
						runner.lastResult = req.theFunction.apply(req._this, req.arguments);
						runner.lastError = null;
					} catch (err) {
						runner.lastResult = null;
						runner.lastError = err;
						logger.log(err);
					}
				}
			}
			req.invoke = invoke;
			return req;
		}
	};
	runners[name] = runner;
	return runner;
}

function get(name, theFunction) {
	var runner = runners[name];
	if (runner == undefined) {
		runner = create(name);
	}
	return runner.request(theFunction);
}

module.service("single-runner", SingleRunner);
SingleRunner.$inject = ["slavi-logger"];
function SingleRunner(logger) {
	this.create = create;
	this.get = get;
}
