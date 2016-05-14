app.controller('BatchController', function($scope, $location, $rootScope, $route, $routeParams, $filter, ProductService, BatchService, MessageService, config) {
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
    };

    $scope.resetConfirmationData = function() {
        $scope.confirmation = {
            updatedProducts: BatchService.getUpdatedProducts(),
        };
    };

    $scope.confirmUpdatedProductsAction = function(form) {
        // si hay productos para actualizar
        if ($scope.confirmation.updatedProducts.length > 0) {
            var request = ProductService.saveBunch($scope.confirmation.updatedProducts);

            request.success = function(response) {
                MessageService.message(MessageService.text('stock de los productos', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');
                BatchService.clearListOfUpdatedProducts();
                $location.path('products');
            };

            request.error = function(response) {
                MessageService.message(MessageService.text('stock de los Productos', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');

                $location.path('batches/bulk_update');
            };

            request.then(request.success, request.error);
        } else { // si no hay productos para actualizar
            $location.path('products');            
        }

    };

    $scope.cancelUpdatedProductsAction = function() {
        // si hay productos para actualizar
        if ($scope.confirmation.updatedProducts.length > 0) {
            // borro todo lo que estaba listo para confirmar
            BatchService.clearListOfUpdatedProducts();
        }
        
        $location.path('products');
    };

    $scope.removeUpdatedProductAction = function(id){
        BatchService.removeUpdatedProduct(id);
    };

    $scope.addBatchesAction = function() {
        $rootScope.setTitle($scope.name, 'Cargar lotes');

        $scope.refreshFormDropdownsData();
    };

    $scope.saveBatchesAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        BatchService.setUpdatedProduct($scope.form.product);

        $location.path('batches/bulk_update');

    };

    $scope.addBatchToTableAction = function() {
        var batch = {
            isoDueDate: $filter('date')($scope.form.batch.isoDueDate, 'yyyyMMdd'),
            stock: $scope.form.batch.stock
        };

        // actualizo la lista de batches con el batch
        $scope.form.product.batches = updateListWithBatch($scope.form.product.batches, batch);

        // limpio los campos de agregar batch
        $scope.form.batch = {};
    };

    function updateListWithBatch(listOfBatches, batch) {
        // busco batch por isoduedate (si no existe devuelve -1 )
        var targetBatchIndex = getBatchIndexByIsoDueDate(listOfBatches, batch.isoDueDate);

        if (targetBatchIndex > -1){ // si existe
            // actualizo el batch con los nuevos valores
            listOfBatches[targetBatchIndex].isoDateToDateObjectueDate = batch.isoDueDate;
            listOfBatches[targetBatchIndex].stock = batch.stock;
        }
        else { // si no existe
            // agrego el nuevo batch a la lista
            listOfBatches.push(batch);
        }

        return listOfBatches;
    };

    function getBatchIndexByIsoDueDate(listOfBatches, batchIsoDueDate) {
        for (var i = 0; i < listOfBatches.length; i++) {
            if (listOfBatches[i].isoDueDate === batchIsoDueDate) {
                return i;
            }
        }
        return -1;
    };

    $scope.removeBatchToTableAction = function(batch) {
        $scope.form.product.batches.forEach(function(item, key) {
            if (item == batch) {
                $scope.form.product.batches.splice(key, 1);
            }
        });
    };

    $scope.editBatchFromTableAction = function(batch) {
        // actualizo el form con los valores del batch a editar
        $scope.form.batch = {
            stock: batch.stock,
            isoDueDate: isoDateToDateObject(batch.isoDueDate)
        }
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

    // funcion que transforma un string ISO del formato yyyyMMdd a un objeto Javascript Date
    function isoDateToDateObject(isoDate){
        var dateObject = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
        
            // regex para formatear la fecha
            var pattern = /(\d{4})(\d{2})(\d{2})/;
            
            // aplico la regex para formatear la fecha al formato ISO 8601: 'yyyy-MM-dd'
            var stringDateISO8601 = isoDateString.replace(pattern, '$1-$2-$3');

            dateObject = new Date(stringDateISO8601);    
        }

        return dateObject;
    }

    $scope.init();
});
