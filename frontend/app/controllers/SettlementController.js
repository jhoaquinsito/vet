/**
 * 
 */
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
    };
    
    
    
    $scope.refreshFormData = function() {
    	var requestPerson  = ClientService.getById(34);
    	var requestSales   = SettlementService.getDueSalesByClientId(34);
        
    	requestPerson.success = function(response){
    		$scope.form.person = response.plain();
    		
    		//Analizamos si tiene LastName
    		if (typeof($scope.form.person.lastname) !== 'undefined')
    			$scope.form.person.lastname =  $scope.form.person.lastname + ", "
    		else
    			$scope.form.person.lastname =  ""
    	}
    	requestPerson.error = function(response) {
            MessageService.message(MessageService.text('person', 'get', 'error', 'male'), 'danger');
        };
        
        requestSales.success = function(response){
    		$scope.form.sales 			= response.plain();
    		$scope.form.totalDeVentas 	= $scope.calculateAllSalesTotal();
    		$scope.form.balance   		= $scope.form.totalDeVentas  - $scope.calculateAllSalesTotal()
    	}
        requestSales.error = function(response) {
            MessageService.message(MessageService.text('sales', 'get', 'error', 'male'), 'danger');
        };
        
        requestPerson.then(requestPerson.success, requestPerson.error);
        requestSales.then(requestSales.success, requestSales.error);
        
        
    	/*
    	$scope.form.person 			= ClientService.getById(34);
        $scope.form.sales 			= SettlementService.getDueSalesByClientId(34);
        */
        $scope.form.newSettlement 	= {
        		amount : 0,
        		concept : "Concepto No Definido",
        		discounted: false
            };
    };
    
    
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
            MessageService.message(MessageService.text('pago', 34 == null ? 'add' : 'edit', 'success', 'male'), 'success');
            
            $scope.refreshFormData();
            //$scope.refreshFormDropdownsData();
            //$location.path('settlement');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('pago', 34 == null ? 'add' : 'edit', 'error', 'male'), 'danger');

            //$location.path('settlement');
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
    
    //TODO - NO BORRAR!
    //TODO - No se usa MAS
    $scope.calculateSaldoAFavor = function(){
    	
    	//TODO - Aca hay que invocar a un método del SettlementService que permita
    	//obtener para el cliente en cuesti{on, cuanto saldo a favor tiene, que seria...
    	//tener todos los Settlement que no hayan sido descontados...
    	
    	if($scope.form.person != null){
    		($scope.calculateAllSalesTotal()) - ($scope.calculateAllSalesTotal())  
    	}else
    		return 0;
    		
    	//return SettlementService.calculateClienteBalance($scope.form.person);
    }
    
  //TODO - NO BORRAR!
    $scope.calculateSettlementsTotal = function(){
    	
    	//TODO - Aca hay que invocar a un método del SettlementService que permita
    	//obtener para el cliente en cuesti{on, cuanto ha entregado , que seria...
    	//tener todos los Settlement y hacer su sumatoria
    	
    	if($scope.form.person != null)
    	return SettlementService.calculateSettlementsTotal($scope.form.person);
    }
    

    $scope.init();
});
