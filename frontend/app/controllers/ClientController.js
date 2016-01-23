app.controller('ClientController', function($scope, $location, $rootScope, $route, $routeParams, ClientService, MessageService, config) {
    $scope.name = 'Clientes';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.clients = [];

    $scope.init = function() {
        switch ($scope.action) {
            case 'client.list':
                $scope.listClientsAction();
                break;
            case 'client.add':
                $scope.addClientAction();
                break;
            case 'client.edit':
                $scope.editClientAction();
                break;
        }
    };

    $scope.listClientsAction = function() {
        $rootScope.setTitle($scope.name, 'Listado de clientes');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;

        $scope.refreshTableData();
    };

    $scope.addClientAction = function() {
        $rootScope.setTitle($scope.name, 'Agregar cliente');

        //$scope.refreshFormDropdownsData();
    };

    $scope.editClientAction = function() {
        $rootScope.setTitle($scope.name, 'Editar cliente');

        $scope.refreshFormData();
        //$scope.refreshFormDropdownsData();
    };

    $scope.removeClientAction = function(clientId) {
        MessageService.confirm(MessageService.text('cliente', 'remove', 'confirm', 'male')).then(function() {
            var request = ClienteService.remove(clientId);

            request.success = function(response) {
                MessageService.message(MessageService.text('cliente', 'remove', 'success', 'male'), 'success');
            };
            request.error = function(response) {
                MessageService.message(MessageService.text('cliente', 'remove', 'error', 'male'), 'danger');
            };

            request.then(request.success, request.error);
        });
    };

    $scope.saveClientAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = ClientService.save($scope.form.client);

        request.success = function(response) {
            MessageService.message(MessageService.text('cliente', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');

            $location.path('clients');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('cliente', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');

            $location.path('clients');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshTableData = function() {
        ClientService.getList().then(function(response) {
            $scope.clients = response.plain();
        });
    };

    $scope.resetFormData = function() {
        $scope.form.client = {
            name: null,
            cuit: null,
            direccion: null,
            celular: null
        };
    };

    $scope.refreshFormData = function() {
        var request = ClientService.getById($routeParams.id);

        request.success = function(response) {
            $scope.form.client = response.plain();
        };
        request.error = function(response) {
            MessageService.message('El cliente solicitado no existe', 'danger');

            $location.path('clients');
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

    $scope.init();
});



