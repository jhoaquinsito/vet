app.factory('ReportSalesService', function($http, Restangular, config,$filter) {
	//Nota: Es importante saber que a la fecha, Settlement no tiene un servicio propio.
	//Un settlement es responsabilidad de un Client, y por eso se usa dicho servicio.
    //var serviceReport 	= Restangular.service('products');
    
    this.executeReport = function(form) {
    	var data;
    	

    	data = {
	            "id": 	 form.formOfSale.id,
	            "description": form.formOfSale.description,
	            "beginDate": dateObjectToIsoDate(form.beginIsoDueDate),
	            "endDate": 	 dateObjectToIsoDate(form.endIsoDueDate)
	        };

    	console.log(data);
        var url = config.API_BASE_URL + '/sale/report';
        return $http.post(url, data);
    };
   
    this.getInvoiceOptions = function() {
        var invoiceOptions = [
          	{label: 'Todas', value: 'Todas'},
            {label: 'Sin imprimir', value: 'Sin imprimir'},
            {label: 'Controlador fiscal', value: 'Controlador fiscal'}
        ];
        return invoiceOptions;
    };
    
     // TODO - GGOROSITO - Esto no se uliliza más, reemplazado su uso por el FormOfSaleService
     /*
    this.getPaiedOutOptions = function() {
        var paiedOutOptions = [
            {label: 'Todas', value: 'Todas'},
            {label: 'Contado', value: 'Contado'},
            {label: 'Cuenta corriente', value: 'Cuenta corriente'}
        ];
        return paiedOutOptions;
    };
     */

    
    /**
     * Este método calcula el total de una venta,
     * en funci{on de los precios establecidos en el mismo momento de la venta.
     */
    this.calculateSaleTotal = function(sale){
        var sum = 0;
        sale.saleLines.forEach(function(saleLine){
            var subtotal = (saleLine.unit_Price - (saleLine.unit_Price * saleLine.discount / 100)) * saleLine.quantity;
            sum += subtotal;
        });
        return sum;
    };
    
    /**
     * Este método calcula el total de una venta,
     * en función de los precios actuales de los productos de la líneas de venta.
     */
    this.calculateSaleTotalUpdated = function(sale){
        var sum = 0;
        sale.saleLines.forEach(function(saleLine){
            var subtotal = (saleLine.batch.product.unitPrice - saleLine.batch.product.unitPrice * saleLine.discount / 100) * saleLine.quantity;
            sum += subtotal;
        });
        return sum;
    };
    
    /**
     * Este método calcula el total de una venta,
     * en función de los precios actuales de los productos de la líneas de venta.
     */
    this.calculateAllSalesTotal = function(sales){
        var sum = 0;
        sales.forEach(function(sale){
        	sale.saleLines.forEach(function(saleLine){
                var subtotal = (saleLine.unit_Price - (saleLine.unit_Price * saleLine.discount / 100)) * saleLine.quantity;
                sum += subtotal;
            });
        });
        return sum;
    };
    
    /**
     * Este método calcula el total de una venta,
     * en función de los precios actuales de los productos de la líneas de venta.
     */
    this.calculateAllSalesTotalUpdated = function(sale){
    	var sum = 0;
        sale.saleLines.forEach(function(saleLine){
            var subtotal = this.calculateSaleTotalUpdated(sale);
            sum += subtotal;
        });
        return sum;
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