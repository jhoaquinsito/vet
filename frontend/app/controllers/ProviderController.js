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
