app.controller('SupplierController', function($scope, $location, $rootScope, $route, $routeParams, SupplierService, MessageService, config) {
    $scope.name = 'Proveedores';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.suppliers = [];

    $scope.init = function() {
        switch ($scope.action) {
            case 'supplier.list':
                $scope.listSuppliersAction();
                break;
            case 'supplier.add':
                $scope.addSupplierAction();
                break;
            case 'supplier.edit':
                $scope.editSupplierAction();
                break;
        }
    };

    $scope.listSuppliersAction = function() {
        $rootScope.setTitle($scope.name, 'Listado de proveedores');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;

        $scope.refreshTableData();
    };

    $scope.addSupplierAction = function() {
        $rootScope.setTitle($scope.name, 'Agregar proveedor');
    };

    $scope.editSupplierAction = function() {
        $rootScope.setTitle($scope.name, 'Editar proveedor');

        $scope.refreshFormData();
    };

    $scope.removeSupplierAction = function(supplierId) {
        MessageService.confirm(MessageService.text('proveedor', 'remove', 'confirm', 'male')).then(function() {
            var request = SupplierService.remove(supplierId);

            request.success = function(response) {
                MessageService.message(MessageService.text('proveedor', 'remove', 'success', 'male'), 'success');

                $scope.refreshTableData();
            };
            request.error = function(response) {
                MessageService.message(MessageService.text('proveedor', 'remove', 'error', 'male'), 'danger');
            };

            request.then(request.success, request.error);
        });
    };

    $scope.saveSupplierAction = function(form) {
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

    $scope.refreshTableData = function() {
        SupplierService.getList().then(function(response) {
            $scope.suppliers = response.plain();
        });
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
