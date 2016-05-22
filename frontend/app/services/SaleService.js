app.factory('SaleService', function(Restangular) {
    var service = Restangular.service('sale');

    this.save = function(sale) {
        // valores por default
        if (sale.settlement.amount == null) {
            sale.settlement.amount = this.calculateSaleTotal(sale);
        }

        if (sale.person == null) {
            sale.person = 1;
        }

        // funcion imprimir desactivada
        sale.invoiced = false;

        // hago que solo mande los id de los batches
        sale.saleLines.forEach(function(saleLine, index, saleLines){
            saleLines[index].batch = saleLine.batchId;
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

    this.updateSaleLinesWithNewSaleLine = function(listOfSaleLines, newSaleLine){

        // intento obtener el indice del batch en la lista de sale lines
        var updatedBatchIndex = arrayGetIndexOfBatchId(listOfSaleLines, newSaleLine.batchId);

        // si existe en la lista
        if (updatedBatchIndex > -1){
            //sumo 1 a la cantidad
            listOfSaleLines[updatedBatchIndex].quantity = listOfSaleLines[updatedBatchIndex].quantity + newSaleLine.quantity;
        } else { // si no existe en la lista
            //agrego el batch a la lista
            listOfSaleLines.push(newSaleLine);
        }


        return listOfSaleLines;
    }

    function arrayGetIndexOfBatchId(array, id) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].batchId === id) {
                return i;
            }
        }
        return -1;
    };

    // funcion que agrega los datos de las unidades a vender a los lotes de un producto
    this.populateProductWithSaleLineUnitsToSell = function(product, saleLines){
        for (var i = 0; i < saleLines.length; i++){
            // intento obtener el indice del batch en los batches del producto
            var batchToPopulateIndex = arrayGetIndexOfId(product.batches, saleLines[i].batchId);

            // si existe en la lista
            if (batchToPopulateIndex > -1){
                //sumo 1 a la cantidad
                product.batches[batchToPopulateIndex].unitsToSell = saleLines[i].quantity;
            } else { // si no existe en la lista
                // TODO error
            }

        }

        return product;
    }

    function arrayGetIndexOfId(array, id) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].id === id) {
                return i;
            }
        }
        return -1;
    };

    return this;
});
