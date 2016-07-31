app.controller('ReportsController', function($scope, $location, $rootScope, $route, $routeParams, $modal, MessageService) {
    $scope.name = 'Reportes';
    $scope.action = $route.current.action;
    $scope.table = {};
    
    $scope.init = function() {
        switch ($scope.action) {
            case 'reports.list':
                $scope.reportSelectionAction();
                break;
        }
    };
    
    $scope.reportSelectionAction = function() {
        $rootScope.setTitle($scope.name, 'Selección de Reporte');
    };

    $scope.init();
});
