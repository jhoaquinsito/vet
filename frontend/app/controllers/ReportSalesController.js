app.controller('ReportSalesController', function($scope, $location, $rootScope, $route, $routeParams,$filter, $modal, ReportSalesService, MessageService, config) {
    $scope.name = 'Reporte - Ventas';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.init = function() {
        switch ($scope.action) {
            case 'reporteSales.detail':
                $scope.reportsAction();
                break;
        }
    };

    $scope.resetFormData = function() {
        $scope.form = {
    		invoiced: null,
            paied_out: null,
    		beginIsoDueDate: dateObjectToIsoDate($scope.form.beginIsoDueDate),
    		endIsoDueDate: 	 dateObjectToIsoDate($scope.form.endIsoDueDate),
            sales : [],
            totalDeVentas : 0
        };
    };

    $scope.refreshFormDropdownsData = function() {

        $scope.form.invoiceOptions = ReportSalesService.getInvoiceOptions();
        $scope.form.invoiced = $scope.form.invoiceOptions[0].value;


        $scope.form.paiedOutOptions = ReportSalesService.getPaiedOutOptions();
        $scope.form.paied_out = $scope.form.paiedOutOptions[0].value;

    };
    
    $scope.reportsAction = function() {
        $rootScope.setTitle($scope.name, 'Reporte - Ventas');

        $scope.resetFormData();
        $scope.refreshFormDropdownsData();
    };
    
    /*
     * Accion ppal.
     * */
    $scope.executeReport = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }
        
        var request = ReportSalesService.executeReport(angular.copy($scope.form));

        request.success = function(response) {
        	$scope.form.sales 			= response.data;
        	$scope.form.totalDeVentas 	= $scope.calculateAllSalesTotal();
        };
        request.error = function(response) {
        	$scope.form.sales 			= [];
        	//this.text = function(entity, action, type, gender)
            MessageService.message(MessageService.text('Reporte - Ventas.',  'Reportes', 'error', 'male'), 'danger');
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

 // funcion que pasa un objeto Date a un integer ISO
    function dateObjectToIsoDate(dateObject){
        var stringDate = $filter('date')(dateObject, 'yyyyMMdd');
        var isoDate = null;
        // verifico que no sea un NaN (illegal number)
        if (!isNaN(parseInt(stringDate))){
            isoDate = parseInt(stringDate);
        }
        return isoDate;
    }
    
 // funcion que transforma un integer ISO del formato yyyyMMdd a un objeto Javascript Date
    function isoDateToDateObject(isoDate){
        var dateObject = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
            
            var year = isoDateString.substr(0,4);
            var month = isoDateString.substr(4,2);
            var day = isoDateString.substr(6,2);
            
            dateObject = new Date(year,month-1, day); 
        }

        return dateObject;
    }

    // funcion que transforma un integer ISO del formato yyyyMMdd a un string yyyy/MM/dd
    $scope.isoDateToFormattedString = function(isoDate) {
        var formattedString = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
        
            // regex para formatear la fecha
            var pattern = /(\d{4})(\d{2})(\d{2})/;
            
            // aplico la regex para formatear la fecha al formato ISO 8601: 'yyyy-MM-dd'
            formattedString = isoDateString.replace(pattern, '$3/$2/$1');
            //console.log(formattedString);
        }

        return formattedString;
    }
    
    
    
    
    
  //TODO - NO BORRAR!
    $scope.calculateSaleTotal = function(sale){
        return ReportSalesService.calculateSaleTotal(sale);
    };
    //TODO - NO BORRAR!
    $scope.calculateSaleTotalUpdated = function(sale){
        return ReportSalesService.calculateSaleTotalUpdated(sale);
    };
    
    //TODO - NO BORRAR!
    $scope.calculateAllSalesTotal = function(){
    	return ReportSalesService.calculateAllSalesTotal($scope.form.sales);
        
    };
    
    //TODO - Borrar porque NO SE USA.
    $scope.calculateSaldoAFavor = function(){
    	
    	//TODO - Aca hay que invocar a un método del ReportSalesService que permita
    	//obtener para el cliente en cuesti{on, cuanto saldo a favor tiene, que seria...
    	//tener todos los Settlement que no hayan sido descontados...
    	
    	if($scope.form.person != null){
    		($scope.calculateAllSalesTotal()) - ($scope.calculateAllSalesTotal())  
    	}else
    		return 0;
    		
    	
    }
    
    //Se usa multiples veces.
    $scope.calculateSettlementsTotal = function(){
    	
    	//TODO - Aca hay que invocar a un método del ReportSalesService que permita
    	//obtener para el cliente en cuesti{on, cuanto ha entregado , que seria...
    	//tener todos los Settlement y hacer su sumatoria
    	
    	if($scope.form.person != null && $scope.form.person.id != null){
    		$scope.form.totalDePagos = ReportSalesService.calculateClienteBalance($scope.form.person);
    	}else{
    		$scope.form.totalDePagos = 0;    		
    	}
    	return $scope.form.totalDePagos;
    	
    }
    
    

    $scope.init();
});
