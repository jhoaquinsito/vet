app.factory('SaleService', function(Restangular) {
    var service = Restangular.service('sale');

    this.save = function(sale) {
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

    return this;
});
