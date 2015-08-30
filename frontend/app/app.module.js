var app = angular.module('app', ['ngRoute'], function() {

});

app.config(function($locationProvider) {
  $locationProvider.html5Mode(true);
});
