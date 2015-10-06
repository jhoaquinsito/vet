var app = angular.module('app', ['ngRoute', 'restangular', 'ngToast']);

app.config(function($locationProvider, RestangularProvider, config) {
    //modo html5 para url más limpias
    $locationProvider.html5Mode(true);

    //configuración de url base para el uso de servicios REST
    RestangularProvider.setBaseUrl(config.API_BASE_URL);
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
