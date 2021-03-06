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
                if (response.data != null && response.data.message != null){
                    MessageService.message(response.data.message,'danger');
                } else {
                    MessageService.message(MessageService.text('cliente', 'remove', 'error', 'male'), 'danger');
                }
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
            if (response.data != null && response.data.message != null){
                MessageService.message(response.data.message,'danger');
            } else {
                MessageService.message(MessageService.text('cliente', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');
            }

            $location.path('clients');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshTableData = function() {
        ClientService.getList().then(function(response) {
            var clients = response.plain();
            
            clients.forEach(function(client) {
                client.fullName = ClientService.getFullName(client);
            });

            $scope.clients = clients;

        }, function(response){
            MessageService.message(MessageService.text('lista de clientes', 'get', 'error', 'female'), 'danger');
        });
    };
    $scope.$watch('form.client.cuit', function(newVal, oldVal) {
  	  if($scope.form.client.cuit.length > 11) {       
  	    $scope.form.client.cuit = oldVal;
  	  }
  	});
    $scope.$watch('form.client.nationalId', function(newVal, oldVal) {
	  if($scope.form.client.nationalId.length > 8) {       
	    $scope.form.client.nationalId = oldVal;
	  }
	});
    
    $scope.$watch("form.sale.settlement.checkNumber", function(newValue, oldValue){
    	
    	if($scope.form.sale.settlement.checkNumber === '' )
		{
    		$scope.form.sale.settlement.date = '';
		}    		
    });

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
            if (response.data != null && response.data.message != null){
                MessageService.message(response.data.message,'danger');
            } else {
                MessageService.message(MessageService.text('cliente', 'get', 'error', 'male'), 'danger');
            }

            $location.path('clients');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshFormDropdownsData = function() {
        IvaCategoryService.getList().then(function(response) {
            $scope.form.ivacategories = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de categorias de IVA', 'get', 'error', 'female'), 'danger');
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

    $scope.init();
});
