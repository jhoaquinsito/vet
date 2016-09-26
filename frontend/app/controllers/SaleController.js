app.controller('SaleController', function($scope, $location, $rootScope, $route, $routeParams, $modal, SaleService, ProductService, ClientService,FormOfSaleService, MessageService, config) {
    $scope.name = 'Ventas';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};
    $scope.form.searchProductModal = {};

    $scope.init = function() {
        switch ($scope.action) {
            case 'sale.add':
                $scope.addSaleAction();
                break;
        }
    };

    $scope.resetFormData = function() {
        $scope.form.sale = {
            invoiced: false,
            paiedOut: false,
            formOfSale: {
            	id: 1,
            	description: "Contado"
            },
            person: null,
            saleLines: [],
            settlement: {
                date: null,
                amount: null,
                concept: null,
                checkNumber: null,
                discounted: null
            }
        };
    };

    $scope.$watch("form.sale.settlement.checkNumber", function(newValue, oldValue){
    	
    	if($scope.form.sale.settlement.checkNumber === '' )
		{
    		$scope.form.sale.settlement.date = '';
		}    		
    });

    $scope.$watch("form.sale.formOfSale", function(newValue, oldValue){
        
        if($scope.form.sale.formOfSale.id === 1 ) // pago contado
        {
            $scope.form.sale.paiedOut = true;
            $scope.form.sale.settlement.discounted = $scope.form.sale.settlement.amount;
        } else { // cuenta corriente
            $scope.form.sale.paiedOut = false;
            $scope.form.sale.settlement.discounted = 0;
        }           
    });
    
    $scope.addSaleAction = function() {
        $rootScope.setTitle($scope.name, 'Realizar venta');

        $scope.resetFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.saveSaleAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = SaleService.save(angular.copy($scope.form.sale));

        request.success = function(response) {
            MessageService.message(MessageService.text('venta', $routeParams.id == null ? 'add' : 'edit', 'success', 'female'), 'success');

            $scope.resetFormData();

            $location.path('sales');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('venta', $routeParams.id == null ? 'add' : 'edit', 'error', 'female'), 'danger');

            $location.path('sales');
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

    $scope.refreshFormDropdownsData = function() {
        ClientService.getList().then(function(response) {
            var clients = response.plain();

            clients.forEach(function(client) {
                client.fullName = client.name + (client.lastName ? ' ' + client.lastName : '');
            });

            $scope.form.clients = clients;
        });

        ProductService.getList().then(function(response) {
            $scope.form.products = response.plain();
        });

        $scope.form.invoiceOptions = SaleService.getInvoiceOptions();
        $scope.form.sale.invoiced = $scope.form.invoiceOptions[0].value;


        

        FormOfSaleService.getList().then(function(response) {
        	//TODO - GGorosito: Es está la forma correcta? 
        	
            var formOfSaleList = response.plain();
            $scope.form.formOfSaleOptions = formOfSaleList;
            //
            $scope.form.formOfSale = $scope.form.formOfSaleOptions[0].value;
        });
    };

    $scope.addSaleLineAction = function() {
        if (event.keyCode != 13) {
            return null;
        }

        if ($scope.form.productBatchCode != null) {
            ProductService.getByBatchCode($scope.form.productBatchCode).then(function(response) {
                var newSaleLine = {};
                var productForSale = response.data;

                newSaleLine.batchId = productForSale.batchId;
                newSaleLine.batchISODueDate = productForSale.batchISODueDate;
                newSaleLine.unit_price = productForSale.unitPrice;
                newSaleLine.productName = productForSale.name;
                newSaleLine.productId = productForSale.id;
                newSaleLine.productMeasureUnitAbbreviation = productForSale.measureUnitAbbreviation;
                newSaleLine.quantity = 1;
                newSaleLine.discount = 0;

                $scope.form.sale.saleLines = SaleService.updateSaleLinesWithNewSaleLine($scope.form.sale.saleLines, newSaleLine);
            });

            // reset del productBatchCode que usó para buscar producto
            $scope.form.productBatchCode = null;
        }
    };

    $scope.calculateSaleTotal = function() {
        return SaleService.calculateSaleTotal($scope.form.sale);
    };


    var productSearchModal = $modal({
        title: 'Buscar producto',
        scope: $scope,
        templateUrl: 'app/views/sale/search-product-modal.html',
        show: false
    });

    $scope.showProductSearchModal = function(selectedProductId) {
        if (selectedProductId != null) {
            // cargo el producto
            ProductService.getById(selectedProductId).then(function(response) {
                var product = response.plain();

                // pasar los sale lines al producto
                $scope.form.searchProductModal.product = SaleService.populateProductWithSaleLineUnitsToSell(product, $scope.form.sale.saleLines);
                // muestro 
                productSearchModal.$promise.then(productSearchModal.show);
            });

        } else {
            productSearchModal.$promise.then(productSearchModal.show);
        }

    };

    $scope.hideProductSearchModal = function() {
        productSearchModal.$promise.then(productSearchModal.hide);
    };

    $scope.resetProductSearchModal = function() {
        $scope.form.searchProductModal.product = null;
    };

    $scope.acceptProductSearchModal = function() {
        $scope.form.searchProductModal.product.batches.forEach(function(batch, index, batches) {
            if (batch.unitsToSell != undefined) {
                // creo una sale line
                var newSaleLine = {};
                newSaleLine.batchId = batch.id;
                newSaleLine.batchISODueDate = batch.isoDueDate;
                newSaleLine.unit_price = $scope.form.searchProductModal.product.unitPrice;
                newSaleLine.productName = $scope.form.searchProductModal.product.name;
                newSaleLine.productId = $scope.form.searchProductModal.product.id;
                newSaleLine.productMeasureUnitAbbreviation = $scope.form.searchProductModal.product.measureUnit.abbreviation;
                newSaleLine.quantity = batch.unitsToSell;
                newSaleLine.discount = 0;

                // actualizo la lista de salelines con la nueva sale line
                $scope.form.sale.saleLines = SaleService.updateSaleLinesWithNewSaleLine($scope.form.sale.saleLines, newSaleLine);
            }
        });

        $scope.hideProductSearchModal();

        $scope.resetProductSearchModal();
    };

    $scope.cancelProductSearchModal = function() {
        $scope.hideProductSearchModal();
        $scope.resetProductSearchModal();
    };

    // funcion que transforma un integer ISO del formato yyyyMMdd a un string yyyy/MM/dd
    $scope.isoDateToFormattedString = function(isoDate) {
        var formattedString = null;

        if (isoDate != null) {
            var isoDateString = isoDate.toString();

            // regex para formatear la fecha
            var pattern = /(\d{4})(\d{2})(\d{2})/;

            // aplico la regex para formatear la fecha al formato ISO 8601: 'yyyy-MM-dd'
            formattedString = isoDateString.replace(pattern, '$1/$2/$3');
        }

        return formattedString;
    };

    // funcion que recibe todas las lineas de venta de un producto y calcula la suma total de la cantidad de ese producto a vender.
    // NOTA: pueden existir varias lineas de ventas para un mismo producto porque las lineas de venta se generan por lote de producto
    $scope.addUpQuantitiesOfAGivenProduct = function(saleLinesOfAGivenProduct) {
        return SaleService.addUpQuantitiesOfAGivenProduct(saleLinesOfAGivenProduct);
    };

    $scope.calculateProductSubtotal = function(saleLinesOfAGivenProduct) {
        return SaleService.calculateProductSubtotal(saleLinesOfAGivenProduct);
    };

    $scope.initializeUnitsToSell = function(index) {
        if ($scope.form.searchProductModal.product.batches[index].unitsToSell == null) {
            $scope.form.searchProductModal.product.batches[index].unitsToSell = 0;
        }
    };

    $scope.removeProductSaleLinesAcion = function(productIdToRemove) {
        $scope.form.sale.saleLines = SaleService.filterSaleLinesWithProductId($scope.form.sale.saleLines, productIdToRemove);
    };

    $scope.hasStock = function(item) {
        return item.stock > 0;
    };

    $scope.hasAvailableBatches = function() {
        for (var i = 0; i < $scope.form.searchProductModal.product.batches.length; i++) {
            // si el lote tiene más de 0 de stock, entonces el lote tiene batches disponibles
            if ($scope.form.searchProductModal.product.batches[i].stock > 0) {
                return true;
            }
        }

        return false;
    };

    // verifico si el boton de aceptar en el modal de busqueda de productos/lote debe estar deshabilitado o no
    $scope.isModalAcceptButtonDisabled = function(){
        // si no tiene ningun producto cargado en el modal entonces tiene que estar deshabilitado
        if ($scope.form.searchProductModal.product != null){
            if ($scope.form.searchProductModal.product.batches != null){
                // si todos los batches están en cero o vacíos entonces tiene que estar deshabilitado
                for (var i = 0; i < $scope.form.searchProductModal.product.batches.length; i++) {
                    if ($scope.form.searchProductModal.product.batches[i].unitsToSell != null && 
                            $scope.form.searchProductModal.product.batches[i].unitsToSell != 0){
                        return false;
                    } 
                }
            }
        }

        return true;
    }

    $scope.init();
});
