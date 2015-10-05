var app = angular.module('app', ['ngRoute', 'restangular']);

app.config(function($locationProvider, RestangularProvider, config) {
    //modo html5 para url más limpias
    $locationProvider.html5Mode(true);

    //configuración de url base para el uso de servicios REST
    RestangularProvider.setBaseUrl(config.API_BASE_URL);
});
