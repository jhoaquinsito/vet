app.controller('SaleController', function($scope, $location, $rootScope, $route, $routeParams, SaleService, MessageService, config) {
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
    };

    $scope.saveSaleAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = SaleService.save($scope.form.sale);

        request.success = function(response) {
            MessageService.message(MessageService.text('proveedor', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');

            $location.path('sales');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('proveedor', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');

            $location.path('sales');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshFormData = function() {
        var request = SaleService.getById($routeParams.id);

        request.success = function(response) {
            $scope.form.sale = response.plain();
        };
        request.error = function(response) {
            MessageService.message('El proveedor solicitado no existe', 'danger');

            $location.path('sales');
        };

        request.then(request.success, request.error);
    };

    $scope.resetFormData = function() {
        $scope.form.sale = {
            name: null,
            cuit: null
        };
    };

    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };

    $scope.init();
});
