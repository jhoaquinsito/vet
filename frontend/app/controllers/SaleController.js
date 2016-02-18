app.controller('SaleController', function($scope, $location, $rootScope, $route, $routeParams, SaleService, MessageService, config) {
    $scope.name = 'Ventas';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.suppliers = [];

    $scope.init = function() {
        switch ($scope.action) {
            case 'supplier.add':
                $scope.addSupplierAction();
                break;
        }
    };

    $scope.addSupplierAction = function() {
        $rootScope.setTitle($scope.name, 'Agregar proveedor');
    };

    $scope.saveSaleAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = SupplierService.save($scope.form.supplier);

        request.success = function(response) {
            MessageService.message(MessageService.text('proveedor', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');

            $location.path('suppliers');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('proveedor', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');

            $location.path('suppliers');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshFormData = function() {
        var request = SupplierService.getById($routeParams.id);

        request.success = function(response) {
            $scope.form.supplier = response.plain();
        };
        request.error = function(response) {
            MessageService.message('El proveedor solicitado no existe', 'danger');

            $location.path('suppliers');
        };

        request.then(request.success, request.error);
    };

    $scope.resetFormData = function() {
        $scope.form.supplier = {
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
