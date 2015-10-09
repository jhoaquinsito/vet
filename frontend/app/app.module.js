var app = angular.module('app', ['ngAnimate', 'ngRoute', 'ngSanitize', 'restangular', 'mgcrea.ngStrap', 'angularUtils.directives.dirPagination']);

app.config(function($locationProvider, RestangularProvider, paginationTemplateProvider, config) {
    //modo html5 para url más limpias
    $locationProvider.html5Mode(true);

    //configuración de url base para el uso de servicios REST
    RestangularProvider.setBaseUrl(config.API_BASE_URL);

    //configuración de path del template para el paginado de las tablas
    paginationTemplateProvider.setPath('app/views/layout/table-pagination-view.html');
});

app.run(function($rootScope) {
    $rootScope.layout = {};
    $rootScope.layout.isMiniSidebar = false;

    $rootScope.$on('$routeChangeSuccess', function(scope, current, previous) {
        //asigna la primera parte de la acción de la ruta (que corresponde a la funcionalidad en general)
        $rootScope.layout.activeSidebarOption = current.action.split('.')[0];
    });

    $rootScope.setTitle = function(title, subtitle) {
        $rootScope.layout.title = title;
        $rootScope.layout.subtitle = subtitle;
    };

    $rootScope.toggleSidebar = function() {
        $rootScope.layout.isMiniSidebar = !$rootScope.layout.isMiniSidebar;
    };
});
