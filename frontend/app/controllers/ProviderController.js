app.controller('ProviderController', function($scope, $location, $rootScope, $route, $routeParams, ProviderService, MessageService, config) {
    $scope.name = 'Proveedores';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.providers = [];

    $scope.init = function() {
        switch ($scope.action) {
            case 'provider.list':
                $scope.listProvidersAction();
                break;
            case 'provider.add':
                $scope.addProviderAction();
                break;
            case 'provider.edit':
                $scope.editProviderAction();
                break;
        }
    };

    $scope.listProvidersAction = function() {
        $rootScope.setTitle($scope.name, 'Listado de proveedores');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;

        $scope.refreshTableData();
    };

    $scope.addProviderAction = function() {
        $rootScope.setTitle($scope.name, 'Agregar proveedor');
    };

    $scope.editProviderAction = function() {
        $rootScope.setTitle($scope.name, 'Editar proveedor');

        $scope.refreshFormData();
    };

    $scope.removeProviderAction = function(providerId) {
        MessageService.confirm(MessageService.text('proveedor', 'remove', 'confirm', 'male')).then(function() {
            var request = ProviderService.remove(providerId);

            request.success = function(response) {
                MessageService.message(MessageService.text('proveedor', 'remove', 'success', 'male'), 'success');
            };
            request.error = function(response) {
                MessageService.message(MessageService.text('proveedor', 'remove', 'error', 'male'), 'danger');
            };

            request.then(request.success, request.error);
        });
    };

    $scope.saveProviderAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = ProviderService.save($scope.form.provider);

        request.success = function(response) {
            MessageService.message(MessageService.text('proveedor', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');

            $location.path('providers');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('proveedor', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');

            $location.path('providers');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshFormData = function() {
        var request = ProviderService.getById($routeParams.id);

        request.success = function(response) {
            $scope.form.product = response.plain();
        };
        request.error = function(response) {
            MessageService.message('El proveedor solicitado no existe', 'danger');

            $location.path('providers');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshTableData = function() {
        ProviderService.getList().then(function(response) {
            $scope.providers = response.plain();
        });
    };

    $scope.resetFormData = function() {
        $scope.form.provider = {
            businessName: null,
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
