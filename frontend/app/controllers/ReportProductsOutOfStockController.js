app.controller('ReportProductsOutOfStockController', function($scope, $location, $rootScope, $route, $routeParams, $modal, ReportProductsOutOfStockService, ProductService, MessageService, config) {
    $scope.name = 'Reportes';
    $scope.action = $route.current.action;
    $scope.table = {};
    
    $scope.init = function() {
        switch ($scope.action) {
            case 'reportProductsOutOfStock.list':
                $scope.productsOutOfStockAction();
                break;
        }
    };
    
    $scope.productsOutOfStockAction = function() {
        $rootScope.setTitle($scope.name, 'Listado de productos a reponer');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;

        $scope.refreshTableData();
    };

    $scope.refreshTableData = function() {
        ReportProductsOutOfStockService.getList().then(function(response) {
            $scope.products = response.data;
        }, function(response){
            MessageService.message(MessageService.text('lista de productos sin stock', 'get', 'error', 'female'), 'danger');
        });
    };

    $scope.calculateProductStock = function(pProduct){
        return ProductService.calculateStock(pProduct);
    }

    $scope.init();
});
