app.factory('SettlementService', function(Restangular) {
	//Nota: Es importante saber que a la fecha, Settlement no tiene un servicio propio.
	//Un settlement es responsabilidad de un Client, y por eso se usa dicho servicio.
    var serviceClient 	= Restangular.service('client');
    var serviceDueSales = Restangular.service('duesales');
    var serviceLegalPerson = Restangular.service('legalperson');
    var serviceNaturalPerson = Restangular.service('naturalperson');
    
    //TODO - Esta función está identica en el ClientService.
    this.save = function(client) {
        if (client.cuit != null) {
            client.client = true;
            return serviceLegalPerson.post(client);
        } else {
            return serviceNaturalPerson.post(client);
        }
    };
    
    this.getDueSalesByClientId = function(clientId) {
        return serviceDueSales.one(clientId).get();
    };
    
    
    /**
     * Este método incorpora un nuevo pago (Settlement)
     * en la lista original del cliente.
     * Y luego hace el Save. correspondiente.
     */
   this.addSettlement = function(client,settlement){
	   var settlements = client.settlements;
	   client.settlements.push(settlement);
	   return this.save(client);
   }


   
    /**
	 * Este método devuelve el total de los pagos 
	 * no descontados realizados por el cliente.
	 */
    //TODO - Esta funcion no se usa más ahora!
    this.calculateClienteBalance = function(person){
    	 var sum = 0;
         
    	 person.settlements.forEach(function(settlement){
    		 if(!settlement.discounted){
    			 var subtotal = settlement.amount;
                 sum += subtotal;
    		 }
         });
    	 
         return sum;
    }
    /**
     * Este método devuelve el total de todas las entregas (Settlement)
     * realizadas por el cliente, sin importar si está descontada o no.
     */
    this.calculateSettlementsTotal = function(person){
    	var sum = 0;
    	person.settlements.forEach(function(settlement){
            var subtotal = settlement.amount;
            sum += subtotal;
        });
        return sum;
    }
    
    
    
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
            var subtotal = (saleLine.product.unitPrice - saleLine.product.unitPrice * saleLine.discount / 100) * saleLine.quantity;
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
                var subtotal = (saleLine.product.unitPrice - saleLine.product.unitPrice * saleLine.discount / 100) * saleLine.quantity;
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
    
    

    return this;
});
