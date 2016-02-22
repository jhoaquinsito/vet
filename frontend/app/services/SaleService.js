app.factory('SaleService', function(Restangular) {
    var service = Restangular.service('sale');

    this.save = function(sale) {
        // valores por default
        if (sale.settlement.amount == null) {
            sale.settlement.amount = calculateSaleTotal();
        }

        // hago que solo mande los id de los productos
        sale.saleLines.forEach(function(saleLine, index, saleLines){
            saleLines[index].product = saleLine.product.id;
        });

        return service.post(sale);
    };

    this.getInvoiceOptions = function() {
    	var invoiceOptions = [
	        {label: 'Sin imprimir', value: false},
	        {label: 'Controlador fiscal', value: true}
    	];
    	return invoiceOptions;
	};

    this.getPaiedOutOptions = function() {
        var paiedOutOptions = [
            {label: 'Contado', value: true},
            {label: 'Cuenta corriente', value: false}
        ];
        return paiedOutOptions;
    };

    this.calculateSaleTotal = function(sale){
        var sum = 0;
        sale.saleLines.forEach(function(saleLine){
            var subtotal = (saleLine.unit_price - saleLine.unit_price * saleLine.discount / 100) * saleLine.quantity;
            sum += subtotal;
        });
        return sum;
    };

    return this;
});
