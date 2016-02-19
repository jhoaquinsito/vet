app.controller('SaleController', function($scope, $location, $rootScope, $route, $routeParams, SaleService, ClientService, MessageService, config) {
    $scope.name = 'Ventas';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.sales = [];

    $scope.init = function() {
        switch ($scope.action) {
            case 'sale.add':
                $scope.addSaleAction();
                break;
        }
    };

    $scope.addSaleAction = function() {
        $rootScope.setTitle($scope.name, 'Realizar venta');

        $scope.refreshFormDropdownsData();
    };

    $scope.saveSaleAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = SaleService.save($scope.form.sale);

        request.success = function(response) {
            MessageService.message(MessageService.text('venta', $routeParams.id == null ? 'add' : 'edit', 'success', 'female'), 'success');

            $location.path('sales');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('venta', $routeParams.id == null ? 'add' : 'edit', 'error', 'female'), 'danger');

            $location.path('sales');
        };

        request.then(request.success, request.error);
    };

    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };

    $scope.refreshFormDropdownsData = function() {
        ClientService.getList().then(function(response) {
            $scope.form.clients = response.plain();
        });
    };

    $scope.init();
});
