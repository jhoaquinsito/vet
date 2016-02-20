app.controller('BatchController', function($scope, $location, $rootScope, $route, $routeParams, CategoryService, DrugService, ManufacturerService, MeasureUnitService, PresentationService, SupplierService, ProductService, MessageService, config) {
    $scope.name = 'Productos';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.products = [];

    $scope.init = function() {
        switch ($scope.action) {
            case 'batch.add':
                $scope.addBatchAction();
                break;
        }
    };

    $scope.addBatchAction = function() {
        $rootScope.setTitle($scope.name, 'Actualizar stock');

        $scope.resetFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.saveStockAction = function(form) {
        if ($scope.formValidation(form)) {
            MessageService.message('Debe completar todos los datos obligatorios para poder actualizar el stock', 'danger');
            return null;
        }

        if ($scope.form.stockTable == null || $scope.form.stockTable.length == 0) {
            MessageService.message('Debe agregar algún producto para poder actualizar el stock', 'danger');
            return null;
        }

        MessageService.message('No se pudo actualizar el stock debido a que la funcionalidad no está implementada', 'warning');

        $location.path('products');
    };

    $scope.refreshTableData = function() {
        ProductService.getList().then(function(response) {
            $scope.products = response.plain();
        });
    };

    $scope.resetFormData = function() {
        $scope.form.product = {
            name: null,
            description: null,
            category: null,
            manufacturer: null,
            presentation: null,
            measureUnit: null,
            drug: null,
            supplier: null,
            cost: null,
            utility: null,
            unitPrice: null,
            minimumStock: null
        };
    };

    $scope.refreshFormData = function() {
        var request = ProductService.getById($routeParams.id);

        request.success = function(response) {
            $scope.form.product = response.plain();
        };
        request.error = function(response) {
            MessageService.message('El producto solicitado no existe', 'danger');

            $location.path('products');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshFormDropdownsData = function() {
        CategoryService.getList().then(function(response) {
            $scope.form.categories = response.plain();
        });

        DrugService.getList().then(function(response) {
            $scope.form.drugs = response.plain();
        });

        ManufacturerService.getList().then(function(response) {
            $scope.form.manufacturers = response.plain();
        });

        MeasureUnitService.getList().then(function(response) {
            $scope.form.measureUnits = response.plain();
        });

        PresentationService.getList().then(function(response) {
            $scope.form.presentations = response.plain();
        });

        SupplierService.getList().then(function(response) {
            $scope.form.suppliers = response.plain();
        });

        $scope.form.ivas = ProductService.getIva();
    };

    $scope.addFormDropdownValue = function(attribute) {
        if ($scope.form.product[attribute].id == null) {
            $scope.form.product[attribute] = {id: null, name: $scope.form.product[attribute]};
        }
    };

    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };

    $scope.productsForStockTable = function() {
        var products = [];

        for (var i = 0; i < $scope.products.length; i++) {
            var product = $scope.products[i];

            if (product.inserted !== true) {
                products.push(product);
            }
        }

        return products;
    };

    $scope.addProductToStockTable = function(event) {
        if (event.keyCode != 13 || !angular.isObject($scope.form.product)) {
            return null;
        }

        if ($scope.form.stockTable == null) {
            $scope.form.stockTable = [];
        }

        $scope.form.stockTable.push($scope.form.product);

        for (var i = 0; i < $scope.products.length; i++) {
            var product = $scope.products[i];

            if (product.id == $scope.form.product.id) {
                product.inserted = true;
                break;
            }
        }

        $scope.form.product = null;
    };

    $scope.removeProductFromStockTable = function(productId) {
        for (var i = 0; i < $scope.form.stockTable.length; i++) {
            var product = $scope.form.stockTable[i];

            if (product.id == productId) {
                product.inserted = false;
                $scope.form.stockTable.splice(i, 1);
                break;
            }
        }
    };

    $scope.updateUtility = function(product) {
        product.utility = ProductService.calculateUtility(product.cost, product.unitPrice);
    };

    $scope.updateUnitPrice = function(product) {
        product.unitPrice = ProductService.calculateUnitPrice(product.cost, product.utility);
    };

    $scope.init();
});
