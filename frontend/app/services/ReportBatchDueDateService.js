app.factory('ReportBatchDueDateService', function($http, Restangular, config,$filter) {
	//Nota: Es importante saber que a la fecha, Settlement no tiene un servicio propio.
	//Un settlement es responsabilidad de un Client, y por eso se usa dicho servicio.
    //var serviceReport 	= Restangular.service('products');
    
    this.executeReport = function(form) {
    	var parameters;
    	
    	if(typeof form.isoDueDate != 'undefined' && form.isoDueDate !== null){
    		parameters = {
    	            "days": form.days,
    	            "beginDate": dateObjectToIsoDate(form.isoDueDate)
    	        };
    	}else{
    		parameters = {
    	            "days": form.days
    	        };
    	}
    
        var url = config.API_BASE_URL + '/products/byduedate';
        return $http.get(url, {
            params: parameters
         });
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
    
    return this;
});