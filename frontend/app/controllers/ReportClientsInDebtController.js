app.controller('ReportClientsInDebtController', function($scope, $location, $rootScope, $route, $routeParams, $modal, ReportClientsInDebtService, MessageService, config) {
    $scope.name = 'Deudores';
    $scope.action = $route.current.action;
    $scope.table = {};
    
    $scope.init = function() {
        switch ($scope.action) {
            case 'reportClientsInDebt.list':
                $scope.clientsInDebtAction();
                break;
        }
    };
    
    $scope.clientsInDebtAction = function() {
        $rootScope.setTitle($scope.name, 'Listado de deudores');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;

        $scope.refreshTableData();
    };

    $scope.refreshTableData = function() {
        ReportClientsInDebtService.getList().then(function(response) {
            $scope.clients = response.data;
        }, function(response){
            MessageService.message(MessageService.text('lista de deudores', 'get', 'error', 'female'), 'danger');
        });
    };

    $scope.init();
});
