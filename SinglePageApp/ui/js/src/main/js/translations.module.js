var module = angular.module('translations', ["pascalprecht.translate", "ngSanitize", "ngCookies"]);

module.config(["$translateProvider", function($translateProvider) {
	$translateProvider
		.preferredLanguage('en')
		.fallbackLanguage(['en', 'bg'])
		.useSanitizeValueStrategy('sanitize')
		.useCookieStorage();
}]);


