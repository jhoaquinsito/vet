app.controller('SettlementController', function($scope, $location, $rootScope, $route, $routeParams, $modal, SaleService, SettlementService, ClientService, MessageService, config) {
    $scope.name = 'Cuenta Corriente';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.init = function() {
        switch ($scope.action) {
            case 'settlement.detail':
            	$scope.settlementDetailAction();
                break;
        }
    };

    $scope.settlementDetailAction = function() {
    	$rootScope.setTitle($scope.name, 'Realizar pago');
    	
    	$scope.refreshFormData();
    	$scope.refreshFormDropdownsData();
    };
    
    //Método del scope para encapsular el proceso de obtención de datos de las ventas de un cliente
    $scope.loadSales = function(clientId) {
        //TODO to be removed: check form.person != null
    	$scope.form.personLoaded = true;
		var requestSales   = SettlementService.getDueSalesByClientId(clientId);
		requestSales.success = function(response){
    		$scope.form.sales 			= response.plain();
    		$scope.form.totalDeVentas 	= $scope.calculateAllSalesTotal();
    	}
        requestSales.error = function(response) {
            MessageService.message(MessageService.text('sales', 'get', 'error', 'male'), 'danger');
        };
        requestSales.then(requestSales.success, requestSales.error);
    }

    $scope.loadClientSettlements = function(clientId){

        var request = SettlementService.getSettlements(clientId);
        request.success = function(response){
            $scope.form.clientSettlements = response.data;
        }
        request.error = function(response) {
            MessageService.message(MessageService.text('settlements', 'get', 'error', 'male'), 'danger');
        };
        request.then(request.success, request.error);
    }
    
    //Este Watch permite analizar el dato dle modelo que alberga la persona. Cuando esta cambia, se cargan los datos de sus ventas.
    $scope.$watch("form.person", function(newValue, oldValue){
    	
    	if(typeof $scope.form.person !== 'undefined' && typeof $scope.form.person.id !== 'undefined')
		{
    		$scope.loadSales($scope.form.person.id);
            $scope.loadClientSettlements($scope.form.person.id);
    		
		}else{
            //TODO to be removed: check form.person != null
			$scope.form.personLoaded = null;
		}
			
    		
    });
    //Este watch permite limitar la cnatidad de caracteres en el campo de NUMERO DE CHEQUE
    $scope.$watch('form.newSettlement.checkNumber', function(newVal, oldVal) {
  	  if($scope.form.newSettlement.checkNumber.length > 8) {       
  	    $scope.form.newSettlement.checkNumber = oldVal;
  	  }
  	});
    
    
    //Este método permite limpiar los datos del modelo asociado con el pequeño formulario para agregar un newSettlement
    $scope.refreshFormData = function() {
        $scope.form.newSettlement 	= {
        		date: Date.now(),
        		amount : 0,
        		concept : "No definido.",
        		checkNumber : "0",
        		discounted: 0,
        		paymentMode : "efectivo"
            };
    };
    
    
    //Este método permite refrescar los datos de los DropDown. En este caso solo refresca el del autocomplete de clientes.
    $scope.refreshFormDropdownsData = function() {
        ClientService.getListForDropdown().then(function(response) {
            var clients = response.data;

            clients.forEach(function(client) {
                client.fullName = ClientService.getFullName(client);
            });

            $scope.form.clients = clients;
        }, function(response){
            MessageService.message(MessageService.text('lista de clientes', 'get', 'error', 'female'), 'danger');
        });
    };



    $scope.addSettlement = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        MessageService.confirm('¿Está seguro que desea agregar el siguiente pago por $'+ $scope.form.newSettlement.amount +'?').then(function() {
            //Incluimos la fecha actual en milisegundos
            $scope.form.newSettlement.date = Date.now();

            var request = SettlementService.addSettlement($scope.form.person.id, $scope.form.clientSettlements, $scope.form.newSettlement);

            request.success = function(response) {
            	//Refrescamos los datos del cliente y de sus ventas.
            	$scope.loadSales($scope.form.person.id);
                $scope.loadClientSettlements($scope.form.person.id);
            	
            	//Emitimos el mensaje de éxito
                MessageService.message(MessageService.text('pago', 34 == null ? 'add' : 'edit', 'success', 'male'), 'success');
                
                $scope.refreshFormData();
                
            };
            request.error = function(response) {
            	//Emitimos el mensaje de fallo.
                MessageService.message(MessageService.text('pago', 34 == null ? 'add' : 'edit', 'error', 'male'), 'danger');
            };

            request.then(request.success, request.error);
        });
        
    };

    //TODO - NO BORRAR!
    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };


    //TODO - NO BORRAR!
    $scope.calculateSaleTotal = function(sale){
        return SettlementService.calculateSaleTotal(sale);
    };
    //TODO - NO BORRAR!
    $scope.calculateSaleTotalUpdated = function(sale){
        return SettlementService.calculateSaleTotalUpdated(sale);
    };
    
    //TODO - NO BORRAR!
    $scope.calculateAllSalesTotal = function(){
    	return SettlementService.calculateAllSalesTotal($scope.form.sales);
    };
    
    //Se usa multiples veces.
    $scope.calculateSettlementsTotal = function(){
    	if($scope.form.person != null && $scope.form.person.id != null){
    		$scope.form.totalDePagos = SettlementService.calculateClienteBalance($scope.form.clientSettlements);
    	}else{
    		$scope.form.totalDePagos = 0;    		
    	}
    	return $scope.form.totalDePagos;
    }

    $scope.isSettlementNotDiscounted = function(element) {
        return element.amount - element.discounted > 0;
    }
        
    $scope.init();
});
