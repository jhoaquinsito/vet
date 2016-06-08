var app = angular.module('app', ['ngCookies', 'ngRoute', 'ngSanitize', 'restangular', 'mgcrea.ngStrap', 'angularUtils.directives.dirPagination', 'angular.filter']);

app.config(function($locationProvider, RestangularProvider, paginationTemplateProvider, $datepickerProvider, config) {
    //modo html5 para url más limpias
    $locationProvider.html5Mode(true);

    //configuración de url base para el uso de servicios REST
    RestangularProvider.setBaseUrl(config.API_BASE_URL);

    //configuración de path del template para el paginado de las tablas
    paginationTemplateProvider.setPath('app/views/layout/table-pagination-view.html');

    //configuración predeterminada de los datepickers
    angular.extend($datepickerProvider.defaults, {
        dateFormat: 'dd/MM/yy',
        placement: 'auto',
        autoclose: true,
        iconLeft: 'fa fa-chevron-left',
        iconRight: 'fa fa-chevron-right'
    });
});

app.run(function($rootScope, $cookies) {
    $rootScope.layout = {};
    $rootScope.layout.isLoading = false;
    $rootScope.layout.isMiniSidebar = $cookies.get('layout.isMiniSidebar') == null ? false : JSON.parse($cookies.get('layout.isMiniSidebar'));

    $rootScope.$on('$routeChangeSuccess', function(scope, current, previous) {
        //asigna la primera parte de la acción de la ruta (que corresponde a la funcionalidad en general)
        $rootScope.layout.activeSidebarOption = current.action != null ? current.action.split('.')[0] : null;
    });

    $rootScope.setTitle = function(title, subtitle) {
        $rootScope.layout.title = title;
        $rootScope.layout.subtitle = subtitle;
    };

    $rootScope.toggleSidebar = function() {
        $rootScope.layout.isMiniSidebar = !$rootScope.layout.isMiniSidebar;
        $cookies.put('layout.isMiniSidebar', $rootScope.layout.isMiniSidebar);
    };
});
