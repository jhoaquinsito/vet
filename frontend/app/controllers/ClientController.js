app.controller('ClientController', function($scope, $location, $rootScope, $route, $routeParams, ClientService, IvaCategoryService, MessageService, config) {
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
            case 'client.detail':
                $scope.clientDetailAction();
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

    $scope.clientDetailAction = function() {
        $rootScope.setTitle($scope.name, 'Detalle de cliente');

        $scope.refreshFormData();
    };

    $scope.addClientAction = function() {
        $rootScope.setTitle($scope.name, 'Agregar cliente');

        $scope.resetFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.editClientAction = function() {
        $rootScope.setTitle($scope.name, 'Editar cliente');

        $scope.refreshFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.removeClientAction = function(clientId) {
        MessageService.confirm(MessageService.text('cliente', 'remove', 'confirm', 'male')).then(function() {
            var request = ClientService.remove(clientId);

            request.success = function(response) {
                MessageService.message(MessageService.text('cliente', 'remove', 'success', 'male'), 'success');

                $scope.refreshTableData();
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
            clientType: 'NATURAL_PERSON',
            lastName: null,
            cuit: null,
            address: null,
            zipCode: null,
            nationalId: null,
            ivacategory: null,
            phone: null,
            mobilePhone: null,
            email: null,
            renspa: null
        };
    };

    $scope.refreshFormData = function() {
        var request = ClientService.getById($routeParams.id);

        request.success = function(response) {
            $scope.form.client = response.plain();
            $scope.form.client.clientType = response.cuit == null ? 'NATURAL_PERSON' : 'LEGAL_PERSON';
        };
        request.error = function(response) {
            MessageService.message('El cliente solicitado no existe', 'danger');

            $location.path('clients');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshFormDropdownsData = function() {
        IvaCategoryService.getList().then(function(response) {
            $scope.form.ivacategories = response.plain();
        });
    };

    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };

    // esta funcion devuelve true si el cliente cumple con los criterios de busqueda
    // en caso contrario, devuelve false
    $scope.filtroPorNombreApellidoORazon = function(client){

        // checkeo que el criterio de busqueda no es undefined or null or '' or '     '
        if (!$scope.table.search || !$scope.table.search.trim()) {
            return true;    
        }

        // previo a comparar el criterio con el nombre (o razon social) y apellido del cliente 
        // coloco a todos en lowerCase para que la busqueda sea case insensitive
        var searchCriteria = $scope.table.search.toLowerCase();
        var clientName = client.name.toLowerCase();
        var clientLastName = null;
        // verifico que el lastName no sea undefined or null or '' or '      '
        if (!!client.lastName && !!client.lastName.trim()) {
            clientLastName = client.lastName;
        }

        // checkeo que el criterio de busqueda este dentro del nombre
        if (clientName.indexOf(searchCriteria) != -1){
            return true;
        }

        // checkeo que el criterio de busqueda este dentro del apellido (si existe)
        if (!!clientLastName && (clientLastName.indexOf(searchCriteria) != -1)){
            return true;
        }

        return false;
    }

    $scope.init();
});
