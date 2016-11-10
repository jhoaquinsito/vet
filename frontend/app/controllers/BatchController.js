app.controller('BatchController', function($scope, $location, $rootScope, $route, $routeParams, $filter, ProductService, BatchService, MessageService, DateUtilsService, config) {
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

    // funcion que se ejecuta al abrirse la pantalla Actualizar stock en bulk
    $scope.bulkUpdateBatchesAction = function() {
        $rootScope.setTitle($scope.name, 'Actualizar stock');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;
        $scope.resetConfirmationData();
    };

    // funcion que hace un reset de los datos de la pantalla Actualizar stock
    $scope.resetConfirmationData = function() {
        $scope.confirmation = {
            // reset de la lista de productos actualizados
            updatedProducts: BatchService.getUpdatedProducts(),
        };
    };

    // funcion que se ejecuta al confirmar productos con lotes actualizados/nuevos, en la pantalla Actualizar stock
    $scope.confirmUpdatedProductsAction = function(form) {
        // si hay productos para actualizar
        if ($scope.confirmation.updatedProducts.length > 0) {

            // reconciliacion de lotes de todos los productos
            $scope.confirmation.updatedProducts = BatchService.reconcileProductsBatches($scope.confirmation.updatedProducts);

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

    // funcion que se ejecuta al cancelar en la pantalla Actualizar stock
    $scope.cancelUpdatedProductsAction = function() {
        // si hay productos para actualizar
        if ($scope.confirmation.updatedProducts.length > 0) {
            // borro todo lo que estaba listo para confirmar
            BatchService.clearListOfUpdatedProducts();
        }
        
        $location.path('products');
    };

    // funcion que se ejecuta al eliminar un producto de la pantalla Actualizar stock
    $scope.removeUpdatedProductAction = function(id){
        BatchService.removeUpdatedProduct(id);
    };

    // funcion que se ejecuta al abrir la pantalla Cargar lotes
    $scope.addBatchesAction = function() {
        $rootScope.setTitle($scope.name, 'Cargar lotes a producto');

        $scope.refreshFormDropdownsData();
        
        // si tiene un id como parametro
        if ($routeParams.id != null) {
            loadFormData(parseInt($routeParams.id));
        }
    };
    // funcion que carga el formulario de la pantalla Cargar Lote con los datos de un producto ya actualizado para poder volver a editarlo
    function loadFormData(updatedProductId) {
        $scope.form.product = BatchService.getUpdatedProductById(updatedProductId);
    };

    // funcion que se ejecuta al guardar un producto con lotes actualizados/nuevos usando la pantalla Cargar lotes
    $scope.saveBatchesAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        BatchService.setUpdatedProduct($scope.form.product);

        $location.path('batches/bulk_update');

    };

    // funcion que verifica si el boton de guardar lote a confirmar debe estar deshabilitado o no
    // devuelve: true, si debe estar deshabilitado; false, si no.
    $scope.isSaveButtonDisabled = function(){
        var isBatchSaveDisabled = false;
        if ($scope.form.product != null){
            isBatchSaveDisabled = !BatchService.productHasBatches($scope.form.product);
        }
        return isBatchSaveDisabled;
    }

    // funcion que se ejecuta al agregar/actualizar un lote a la lista de lotes de un producto en la pantalla Cargar lotes
    $scope.addBatchToTableAction = function() {
        var batch = {
            id: $scope.form.batch.id,
            isoDueDate: DateUtilsService.dateObjectToIsoDate($scope.form.batch.isoDueDate),
            stock: $scope.form.batch.stock
        };

        // actualizo la lista de batches con el batch
        if ($scope.form.product.batchesToConfirm == undefined) { $scope.form.product.batchesToConfirm = []; } 
        $scope.form.product.batchesToConfirm = BatchService.updateListWithBatch($scope.form.product.batchesToConfirm, batch);

        // limpio los campos de agregar batch
        $scope.form.batch = {};
    };

    // funcion que se ejecuta al eliminar un lote de la lista de lotes de la pantalla Cargar lote
    $scope.removeBatchToTableAction = function(batch) {
        $scope.form.product = BatchService.removeBatchFromProductBatches($scope.form.product, batch);
    };

    // funcion que se ejecuta al quere editar un lote de la lista de lotes de la pantalla Cargar lote
    $scope.editBatchFromTableAction = function(batch) {
        // actualizo el form con los valores del batch a editar
        $scope.form.batch = {
            id: batch.id,
            stock: batch.stock,
            isoDueDate: DateUtilsService.isoDateToDateObject(batch.isoDueDate)
        };

        // si no tiene id, es porque es nuevo, entonces lo borro de la lista para que se vuelva a agregar
        if (batch.id == null){
            $scope.form.product = BatchService.removeBatchFromProductBatches($scope.form.product, batch);
        }
    };

    // funcion que hace refresh de los datos de los dropdowns en la pantalla Cargar lote
    $scope.refreshFormDropdownsData = function() {
        ProductService.getList().then(function(response) {
            $scope.form.products = response.plain();
        });

        $scope.form.ivas = ProductService.getIvaOptions();
    };

    // funcion que valida el formulario de la pantalla Cargar lote
    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };

    // funcion que actualiza la utilidad de un producto en base al costo y precio unitario en la pantalla de Cargar lote
    $scope.updateUtility = function() {
        $scope.form.product.utility = ProductService.calculateUtility($scope.form.product.cost, $scope.form.product.unitPrice, $scope.form.product.iva);
    };

    // funcion que actualiza el precio unitario de un producto en base al costo y la utilidad en la pantalla de Cargar lote
    $scope.updateUnitPrice = function() {
        $scope.form.product.unitPrice = ProductService.calculateUnitPrice($scope.form.product.cost, $scope.form.product.utility, $scope.form.product.iva);
    };

    // funcion que transforma un integer ISO del formato yyyyMMdd a un string yyyy/MM/dd
    $scope.isoDateToFormattedString = function(isoDate) {
        return DateUtilsService.isoDateToFormattedString(isoDate);
    }   

    $scope.init();
});
