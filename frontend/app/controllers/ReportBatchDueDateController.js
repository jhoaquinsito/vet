app.controller('ReportBatchDueDateController', function($scope, $location, $rootScope, $route, $routeParams,$filter, $modal, ReportBatchDueDateService, MessageService, config) {
    $scope.name = 'Reportes';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.init = function() {
        switch ($scope.action) {
            case 'reporteBatchDueDate.detail':
                $scope.reportsAction();
                break;
        }
    };

    $scope.resetFormData = function() {
        $scope.form = {
            days: 30,
            daysEstablished: 30,
            isoDueDate: dateObjectToIsoDate($scope.form.isoDueDate),
            batches : []
        };
    };

    
    
    $scope.reportsAction = function() {
        $rootScope.setTitle($scope.name, 'Vencimiento de Lotes');

        $scope.resetFormData();
    };
    
    /*
     * Accion ppal.
     * */
    $scope.executeReport = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }
        $scope.form.daysEstablished = $scope.form.days;
        var request = ReportBatchDueDateService.executeReport(angular.copy($scope.form));

        request.success = function(response) {
        	$scope.form.batches 			= response.data;

        };
        request.error = function(response) {
        	//TODO tomas: revisar si esto esta bien:
            $scope.form.batches = [];
            if (response.data != null && response.data.message != null){
                MessageService.message(response.data.message,'danger');
            } else {
                MessageService.message(MessageService.text('reporte de vencimiento de lotes',  'report', 'error', 'male'), 'danger');
            }
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
    // TODO refactor pendiente: metodo repetido y parte de las DateUtils
    $scope.isoDateToFormattedString = function(isoDate) {
        var formattedString = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
        
            // regex para formatear la fecha
            var pattern = /(\d{4})(\d{2})(\d{2})/; //ddmmyyyy var pattern = /(\d{2})(\d{2})(\d{4})/;  - yyyymmdd var pattern = /(\d{4})(\d{2})(\d{2})/;
            
            // aplico la regex para formatear la fecha al formato ISO 8601: 'yyyy-MM-dd'
            formattedString = isoDateString.replace(pattern, '$3/$2/$1'); //'$1/$2/$3'
        }

        return formattedString;
    }
    
    // funcion que retorna la cantidad de días entre: Fecha de Vencimiento de un lote - Fecha del día actual, expresada en días.
    $scope.getDaysBeforeDueDate = function(isoDate) {
        var formattedString = $scope.isoDateToFormattedString(isoDate);
        
        var dueDateParts = formattedString.split("/");
        var dueDate = new Date(parseInt(dueDateParts[2], 10),
                          parseInt(dueDateParts[1], 10) - 1,
                          parseInt(dueDateParts[0], 10));
        
        var today = new Date();
        
        var differenceInDays = 0;
        
        var timeDiff = Math.abs(dueDate.getTime() - today.getTime());
        var differenceInDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
        
        return differenceInDays;
    }
    
    $scope.hasExpired = function(isoDate) {
        var formattedString = $scope.isoDateToFormattedString(isoDate);
        
        var dueDateParts = formattedString.split("/");
        var dueDate = new Date(parseInt(dueDateParts[2], 10),
                          parseInt(dueDateParts[1], 10) - 1,
                          parseInt(dueDateParts[0], 10));
    
    	var today = new Date();
       
        return dueDate < today;
    }
    
    

    $scope.init();
});
