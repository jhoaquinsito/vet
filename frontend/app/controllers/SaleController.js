app.controller('SaleController', function($scope, $location, $rootScope, $route, $routeParams, $modal, SaleService, ProductService, ClientService, MessageService, config) {
    $scope.name = 'Ventas';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

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
            paied_out: null,
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

    $scope.addSaleAction = function() {
        $rootScope.setTitle($scope.name, 'Realizar venta');

        $scope.resetFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.saveSaleAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        

        var request = SaleService.save($scope.form.sale);

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
            $scope.form.clients = response.plain();
        });

        ProductService.getList().then(function(response) {
            $scope.form.products = response.plain();
        });

        $scope.form.invoiceOptions = SaleService.getInvoiceOptions();
        $scope.form.sale.invoiced = $scope.form.invoiceOptions[0].value;


        $scope.form.paiedOutOptions = SaleService.getPaiedOutOptions();
        $scope.form.sale.paied_out = $scope.form.paiedOutOptions[0].value;
        
    };

    $scope.addSaleLineAction = function() {
        if (event.keyCode != 13) {
            return null;
        }

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

            $scope.form.sale.saleLines = SaleService.updateSaleLinesWithNewSaleLine($scope.form.sale.saleLines,newSaleLine);
        });

        // reset del productBatchCode que usó para buscar producto
        $scope.form.productBatchCode = null;
    };

    $scope.calculateSaleTotal = function(){
        return SaleService.calculateSaleTotal($scope.form.sale);
    };


    var productSearchModal = $modal({
        title: 'Buscar producto',
        scope: $scope,
        templateUrl: 'app/views/sale/search-product-modal.html',
        show: false
    });

    $scope.showProductSearchModal = function(){
        productSearchModal.$promise.then(productSearchModal.show);
    };
    
    $scope.hideProductSearchModal = function(){
        productSearchModal.$promise.then(productSearchModal.hide);
    };
    
    $scope.resetProductSearchModal = function(){
        $scope.form.searchProductModal.product = null;
    };

    $scope.acceptProductSearchModal = function(){
        // para cada batch que tiene cant a vender > 0
        $scope.form.searchProductModal.product.batches.forEach(function(batch, index, batches){
            if (batch.unitsToSell > 0){
                // creo una sale line
                var newSaleLine = {};
                newSaleLine.batchId = batch.id;
                newSaleLine.batchISODueDate = batch.isoDueDate;
                newSaleLine.unit_price = $scope.form.searchProductModal.product.unitPrice;
                newSaleLine.productName = $scope.form.searchProductModal.product.name;
                newSaleLine.productId = $scope.form.searchProductModal.product.id;
                newSaleLine.productMeasureUnitAbbreviation = $scope.form.searchProductModal.product.measureUnit.abbreviation;
                newSaleLine.quantity = 1;
                newSaleLine.discount = 0;
                
                // agrego una sale line
                $scope.form.sale.saleLines = SaleService.updateSaleLinesWithNewSaleLine($scope.form.sale.saleLines,newSaleLine);
            }
        });

        $scope.hideProductSearchModal();

        $scope.resetProductSearchModal();
    };

    $scope.cancelProductSearchModal = function(){
        $scope.hideProductSearchModal();
        $scope.resetProductSearchModal();
    };

    $scope.calculatePersonDebt = function(){
        // TODO analizar si corresponde calcular el saldo actual en el frontend o backend
        return 0;
    };

    $scope.init();
});
