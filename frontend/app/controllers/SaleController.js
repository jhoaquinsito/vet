app.controller('SaleController', function($scope, $location, $rootScope, $route, $routeParams, SaleService, ProductService, ClientService, MessageService, config) {
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
            invoiced: null, 
            paied_out: null,
            person: null,
            saleLines: []
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
    };

    $scope.addSaleLineAction = function() {
        if (event.keyCode != 13) {
            return null;
        }

        ProductService.getByBatchCode($scope.form.productBatchCode).then(function(response) {
            var newSaleLine = {};
            var product = response.data;

            newSaleLine.product = product;
            newSaleLine.quantity = 1;
            newSaleLine.unit_price = product.unitPrice;
            newSaleLine.discount = 0;

            $scope.form.sale.saleLines.push(newSaleLine);
        });
    };

    $scope.calculateSaleTotal = function(){
        var sum = 0;
        $scope.form.sale.saleLines.forEach(function(saleLine){
            var subtotal = (saleLine.unit_price - saleLine.unit_price * saleLine.discount / 100) * saleLine.quantity;
            sum += subtotal;
        });
        return sum;
    };

    $scope.init();
});
