app.factory('SaleService', function($filter, Restangular) {
    var service = Restangular.service('sale');

    this.save = function(sale) {
        // valores por default
        if (sale.settlement.amount == null) {
            sale.settlement.amount = sale.paied_out ? this.calculateSaleTotal(sale) : 0;
        }

        if (sale.person == null) {
            sale.person = 1;
        }

        // funcion imprimir desactivada
        sale.invoiced = false;

        // hago que solo mande los id de los batches
        sale.saleLines.forEach(function(saleLine, index, saleLines) {
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

    this.calculateProductSubtotal = function(saleLinesOfAGivenProduct){
        return (saleLinesOfAGivenProduct[0].unit_price - saleLinesOfAGivenProduct[0].unit_price * saleLinesOfAGivenProduct[0].discount / 100) * this.addUpQuantitiesOfAGivenProduct(saleLinesOfAGivenProduct);
    }

    this.calculateSaleTotal = function(sale) {
        var totalAmount = 0;

        if (sale.saleLines.length > 0){
            var saleLinesGroupedByProduct = $filter('groupBy')(sale.saleLines, 'productId');
            
            for (var bKeyValue in saleLinesGroupedByProduct) {
                if(saleLinesGroupedByProduct.hasOwnProperty(bKeyValue)) {
                    totalAmount += this.calculateProductSubtotal(saleLinesGroupedByProduct[bKeyValue]);
                } 
            } 

            //saleLinesGroupedByProduct.forEach(function(saleLinesOfAGivenProduct) {
            //    totalAmount += this.calculateProductSubtotal(saleLinesOfAGivenProduct);
            //});
        }

        return totalAmount;
    };

    this.addUpQuantitiesOfAGivenProduct = function(saleLinesOfAGivenProduct) {
        var total = 0;
        for (var i = 0; i < saleLinesOfAGivenProduct.length; i++) {
            total = saleLinesOfAGivenProduct[i].quantity + total;
        }
        return total;
    }

    this.updateSaleLinesWithNewSaleLine = function(listOfSaleLines, newSaleLine) {

        //TODO SI VIENE CON 0 ENTONCES HAY QUE BORRARLA

        // intento obtener el indice del batch en la lista de sale lines
        var updatedBatchIndex = arrayGetIndexOfBatchId(listOfSaleLines, newSaleLine.batchId);

        // si existe en la lista
        if (updatedBatchIndex > -1) {
            //sumo 1 a la cantidad
            listOfSaleLines[updatedBatchIndex].quantity = newSaleLine.quantity;
        } else { // si no existe en la lista
            //agrego el batch a la lista
            listOfSaleLines.push(newSaleLine);
        }

        listOfSaleLines = filterSaleLinesWithQuantityZero(listOfSaleLines);

        return listOfSaleLines;
    };

    function arrayGetIndexOfBatchId(array, id) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].batchId === id) {
                return i;
            }
        }
        return -1;
    }

    // funcion que recibe una lista de salelines y quita todas las salelines que tienen cantidad cero
    function filterSaleLinesWithQuantityZero(listOfSaleLines) {
        var filteredList = [];

        for (var i = 0; i < listOfSaleLines.length; i++) {
            // si la cantidad es diferente de 0, entonces lo agrego a la lista filtrada
            if (listOfSaleLines[i].quantity != 0) {
                filteredList.push(listOfSaleLines[i]);
            }
        }

        return filteredList;
    }

    // funcion que agrega los datos de las unidades a vender a los lotes de un producto
    this.populateProductWithSaleLineUnitsToSell = function(product, saleLines) {
        for (var i = 0; i < saleLines.length; i++) {
            // intento obtener el indice del batch en los batches del producto
            var batchToPopulateIndex = arrayGetIndexOfId(product.batches, saleLines[i].batchId);

            // si existe en la lista
            if (batchToPopulateIndex > -1) {
                //sumo 1 a la cantidad
                product.batches[batchToPopulateIndex].unitsToSell = saleLines[i].quantity;
            } else { // si no existe en la lista
                // TODO error
            }

        }

        return product;
    };

    function arrayGetIndexOfId(array, id) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].id === id) {
                return i;
            }
        }
        return -1;
    }

    this.filterSaleLinesWithProductId = function(listOfSaleLines, productIdToRemove) {
        var filteredList = [];

        for (var i = 0; i < listOfSaleLines.length; i++) {
            if (listOfSaleLines[i].productId != productIdToRemove) {
                filteredList.push(listOfSaleLines[i]);
            }
        }

        return filteredList;
    };

    return this;
});
