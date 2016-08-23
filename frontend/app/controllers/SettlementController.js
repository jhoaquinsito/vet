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
    	$scope.refreshFormData();
    	$scope.refreshFormDropdownsData();
    };
    
    //Método del scope para encapsular el proceso de obtención de datos de las ventas de un cliente
    $scope.loadSales = function(clientId) {
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
    
    //Método del scope para encapsular el proceso de obtención de datos de un cliente
    $scope.loadCliente = function(personId) {
    	var requestPerson  = ClientService.getById(personId);
    	requestPerson.success = function(response){
    		$scope.$apply(function() {
    		$scope.form.person = response.plain();
    		});
	   	}
    	requestPerson.error = function(response) {
            MessageService.message(MessageService.text('person', 'get', 'error', 'male'), 'danger');
        };
    }
    
    //Este Watch permite analizar el dato dle modelo que alberga la persona. Cuando esta cambia, se cargan los datos de sus ventas.
    $scope.$watch("form.person", function(newValue, oldValue){
    	
    	if(typeof $scope.form.person !== 'undefined' && typeof $scope.form.person.id !== 'undefined')
		{
    		$scope.loadSales($scope.form.person.id);
    		
		}else{
			$scope.form.personLoaded = null;
		}
			
    		
    });
    
    
    //Este método permite limpiar los datos del modelo asociado con el pequeño formulario para agregar un newSettlement
    $scope.refreshFormData = function() {
        $scope.form.newSettlement 	= {
        		amount : 0,
        		concept : "No definido.",
        		paymentMode : "contado",
        		checkNumber : "0",
        		discounted: false
            };
    };
    
    
    //Este método permite refrescar los datos de los DropDown. En este caso solo refresca el del autocomplete de clientes.
    $scope.refreshFormDropdownsData = function() {
        ClientService.getList().then(function(response) {
            $scope.form.clients = response.plain();
        });
    };



    $scope.addSettlement = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        //Incluimos la fecha actual en milisegundos
        $scope.form.newSettlement.date = Date.now();
        
        var request = SettlementService.addSettlement($scope.form.person, $scope.form.newSettlement);

        request.success = function(response) {
        	console.log("response" + response);
        	
        	//Refrescamos los datos del cliente y de sus ventas.
        	$scope.loadSales(response);
        	$scope.loadCliente(response);
        	
        	//Emitimos el mensaje de éxito
            MessageService.message(MessageService.text('pago', 34 == null ? 'add' : 'edit', 'success', 'male'), 'success');
            
            $scope.refreshFormData();
            
        };
        request.error = function(response) {
        	//Emitimos el mensaje de fallo.
            MessageService.message(MessageService.text('pago', 34 == null ? 'add' : 'edit', 'error', 'male'), 'danger');
        };

        request.then(request.success, request.error);
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
    	
    	//TODO - Aca hay que invocar a un método del SettlementService que permita
    	//obtener para el cliente en cuesti{on, cuanto ha entregado , que seria...
    	//tener todos los Settlement y hacer su sumatoria
    	
    	if($scope.form.person != null && $scope.form.person.id != null){
    		$scope.form.totalDePagos = SettlementService.calculateClienteBalance($scope.form.person);
    	}else{
    		$scope.form.totalDePagos = 0;    		
    	}
    	return $scope.form.totalDePagos;
    	
    }
    

    $scope.init();
});
