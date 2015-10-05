var app = angular.module('app', ['ngResource', 'ngRoute']);

app.config(function($locationProvider) {
    $locationProvider.html5Mode(true);
});
