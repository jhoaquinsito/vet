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
        $rootScope.setTitle($scope.name, 'Cargar lotes');

        $scope.refreshFormDropdownsData();
    };

    // funcion que se ejecuta al guardar un producto con lotes actualizados/nuevos usando la pantalla Cargar lotes
    $scope.saveBatchesAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        BatchService.setUpdatedProduct($scope.form.product);

        $location.path('batches/bulk_update');

    };

    // funcion que se ejecuta al agregar/actualizar un lote a la lista de lotes de un producto en la pantalla Cargar lotes
    $scope.addBatchToTableAction = function() {
        var batch = {
            isoDueDate: dateObjectToIsoDate($scope.form.batch.isoDueDate),
            stock: $scope.form.batch.stock
        };

        // actualizo la lista de batches con el batch
        $scope.form.product.batches = updateListWithBatch($scope.form.product.batches, batch);

        // limpio los campos de agregar batch
        $scope.form.batch = {};
    };

    // funcion que pasa un objeto Date a un integer ISO
    function dateObjectToIsoDate(dateObject){
        var stringDate = $filter('date')(dateObject, 'yyyyMMdd');
        var isoDate = null;
        // verifico que no sea un NaN (illegal number)
        if (!isNaN(parseInt(stringDate))){
            isoDate = parseInt(stringDate);
        }
        return isoDate;
    }

    // funcion que actualiza la lista de lotes en la pantalla Cargar lotes con el lote nuevo/modificado
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

    // funcion que busca un lote por fecha de vencimiento en una lista de lotes
    function getBatchIndexByIsoDueDate(listOfBatches, batchIsoDueDate) {
        for (var i = 0; i < listOfBatches.length; i++) {
            if (listOfBatches[i].isoDueDate === batchIsoDueDate) {
                return i;
            }
        }
        return -1;
    };

    // funcion que se ejecuta al eliminar un lote de la lista de lotes de la pantalla Cargar lote
    $scope.removeBatchToTableAction = function(batch) {
        $scope.form.product.batches.forEach(function(item, key) {
            if (item == batch) {
                $scope.form.product.batches.splice(key, 1);
            }
        });
    };

    // funcion que se ejecuta al quere editar un lote de la lista de lotes de la pantalla Cargar lote
    $scope.editBatchFromTableAction = function(batch) {
        // actualizo el form con los valores del batch a editar
        $scope.form.batch = {
            stock: batch.stock,
            isoDueDate: isoDateToDateObject(batch.isoDueDate)
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
        $scope.form.product.utility = ProductService.calculateUtility($scope.form.product.cost, $scope.form.product.unitPrice);
    };

    // funcion que actualiza el precio unitario de un producto en base al costo y la utilidad en la pantalla de Cargar lote
    $scope.updateUnitPrice = function() {
        $scope.form.product.unitPrice = ProductService.calculateUnitPrice($scope.form.product.cost, $scope.form.product.utility);
    };

    // funcion que transforma un integer ISO del formato yyyyMMdd a un objeto Javascript Date
    function isoDateToDateObject(isoDate){
        var dateObject = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
            
            var year = isoDateString.substr(0,4);
            var month = isoDateString.substr(4,2);
            var day = isoDateString.substr(6,2);
            
            dateObject = new Date(year,month-1, day); 
        }

        return dateObject;
    }

    // funcion que transforma un integer ISO del formato yyyyMMdd a un string yyyy/MM/dd
    $scope.isoDateToFormattedString = function(isoDate) {
        var formattedString = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
        
            // regex para formatear la fecha
            var pattern = /(\d{4})(\d{2})(\d{2})/;
            
            // aplico la regex para formatear la fecha al formato ISO 8601: 'yyyy-MM-dd'
            formattedString = isoDateString.replace(pattern, '$1/$2/$3');
        }

        return formattedString;
    }

    $scope.init();
});
