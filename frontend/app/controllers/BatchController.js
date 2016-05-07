app.controller('BatchController', function($scope, $location, $rootScope, $route, $routeParams, $filter, ProductService, MessageService, config) {
    $scope.name = 'Productos';
    $scope.action = $route.current.action;
    $scope.form = {};
    $scope.table = {};
    $scope.confirmation = {};


    $scope.init = function() {
        switch ($scope.action) {
            case 'batch.add':
                $scope.addBatchesAction();
                break;
            case 'batch.bulk_update':
                $scope.bulkUpdateBatchesAction();
                break;
        }
    };

    $scope.bulkUpdateBatchesAction = function() {
        $rootScope.setTitle($scope.name, 'Actualizar stock');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;
        $scope.resetConfirmationData();
        //TODO $scope.refreshFormDropdownsData();
    };

    $scope.resetConfirmationData = function() {
        if (!$scope.confirmation.updatedProducts){
            $scope.confirmation = {
                updatedProducts:[],
            };
        }
    };


    $scope.addBatchesAction = function() {
        $rootScope.setTitle($scope.name, 'Cargar lotes');

        $scope.refreshFormDropdownsData();
    };

    $scope.saveBatchesAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = ProductService.save($scope.form.product);

        request.success = function(response) {
            MessageService.message(MessageService.text('stock del producto', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');

            $location.path('products');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('stock del producto', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');

            $location.path('products');
        };

        request.then(request.success, request.error);
    };

    $scope.addBatchToTableAction = function() {
        var batch = {
            isoDueDate: $filter('date')($scope.form.batch.isoDueDate, 'yyyyMMdd'),
            stock: $scope.form.batch.stock
        };

        $scope.form.product.batches.push(batch);

        $scope.form.batch = {};
    };

    $scope.removeBatchToTableAction = function(batch) {
        $scope.form.product.batches.forEach(function(item, key) {
            if (item == batch) {
                $scope.form.product.batches.splice(key, 1);
            }
        });
    };

    $scope.refreshFormDropdownsData = function() {
        ProductService.getList().then(function(response) {
            $scope.form.products = response.plain();
        });

        $scope.form.ivas = ProductService.getIvaOptions();
    };

    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };

    $scope.updateUtility = function() {
        $scope.form.product.utility = ProductService.calculateUtility($scope.form.product.cost, $scope.form.product.unitPrice);
    };

    $scope.updateUnitPrice = function() {
        $scope.form.product.unitPrice = ProductService.calculateUnitPrice($scope.form.product.cost, $scope.form.product.utility);
    };

    $scope.init();
});
