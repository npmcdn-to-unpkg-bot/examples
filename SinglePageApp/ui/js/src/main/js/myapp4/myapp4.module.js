var module = angular.module('myapp4', ['ngComponentRouter', 'myapp4.heroes', 'myapp4.crisis-center', "slavi-log"]);

module.value('$routerRootComponent', 'app');

module.config(["$locationProvider", function($locationProvider) {
//  $locationProvider.html5Mode(true);
}]);
