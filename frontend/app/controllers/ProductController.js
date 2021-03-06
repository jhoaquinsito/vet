app.controller('ProductController', function($scope, $location, $rootScope, $route, $routeParams, CategoryService, DrugService, ManufacturerService, MeasureUnitService, PresentationService, SupplierService, ProductService, MessageService, config) {
    $scope.name = 'Productos';
    $scope.action = $route.current.action;
    $scope.table = {};
    $scope.form = {};

    $scope.products = [];

    $scope.init = function() {
        switch ($scope.action) {
            case 'product.list':
                $scope.listProductsAction();
                break;
            case 'product.add':
                $scope.addProductAction();
                break;
            case 'product.edit':
                $scope.editProductAction();
                break;
            case 'product.detail':
                $scope.productDetailAction();
                break;
        }
    };

    $scope.listProductsAction = function() {
        $rootScope.setTitle($scope.name, 'Listado de productos');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;

        $scope.refreshTableData();
    };

    $scope.addProductAction = function() {
        $rootScope.setTitle($scope.name, 'Agregar producto');

        $scope.resetFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.editProductAction = function() {
        $rootScope.setTitle($scope.name, 'Editar producto');

        $scope.refreshFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.productDetailAction = function() {
        $rootScope.setTitle($scope.name, 'Detalle de producto');

        $scope.refreshFormData();
    };

    $scope.removeProductAction = function(productId) {
        MessageService.confirm(MessageService.text('producto', 'remove', 'confirm', 'male')).then(function() {
            var request = ProductService.remove(productId);

            request.success = function(response) {
                MessageService.message(MessageService.text('producto', 'remove', 'success', 'male'), 'success');

                $scope.refreshTableData();
            };
            request.error = function(response) {
                if (response.data != null && response.data.message != null){
                    MessageService.message(response.data.message,'danger');
                } else {
                    MessageService.message(MessageService.text('producto', 'remove', 'error', 'male'), 'danger');
                }
            };

            request.then(request.success, request.error);
        });
    };

    $scope.saveProductAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        // asocio categorias con el mismo nombre (no case sensitive)
        matchCategoryIfExists();
        // asocio laboratorios con el mismo nombre (no case sensitive)
        matchManufacturerIfExists();
        // asocio drogas con el mismo nombre (no case sensitive)
        matchDrugIfExists();
        // asocio presentaciones con el mismo nombre (no case sensitive)
        matchPresentationIfExists();

        var request = ProductService.save($scope.form.product);

        request.success = function(response) {
            MessageService.message(MessageService.text('producto', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');

            $location.path('products');
        };
        request.error = function(response) {
            if (response.data != null && response.data.message != null){
                MessageService.message(response.data.message,'danger');
            } else {
                MessageService.message(MessageService.text('producto', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');
            }

            $location.path('products');
        };

        request.then(request.success, request.error);
    };

    function matchCategoryIfExists(){
        // si la categoría no se asoció a un id, reviso si no se puede asociar a otros case insensitive
        if ($scope.form.product.category != null && $scope.form.product.category.id == null) {
            angular.forEach($scope.form.categories, function(category) {
                //si los nombres son iguales en lower case entonces es la misma pero con otro case
                if (category.name.toLowerCase() == $scope.form.product.category.name.toLowerCase()) {
                    $scope.form.product.category.name = category.name;
                    $scope.form.product.category.id = category.id;
                }
            });
        }
    };

    function matchManufacturerIfExists(){
        // si el laboratorio no se asoció a un id, reviso si no se puede asociar a otros case insensitive
        if ($scope.form.product.manufacturer != null && $scope.form.product.manufacturer.id == null) {
            angular.forEach($scope.form.manufacturers, function(manufacturer) {
                //si los nombres son iguales en lower case entonces es la misma pero con otro case
                if (manufacturer.name.toLowerCase() == $scope.form.product.manufacturer.name.toLowerCase()) {
                    $scope.form.product.manufacturer.name = manufacturer.name;
                    $scope.form.product.manufacturer.id = manufacturer.id;
                }
            });
        }
    };

    function matchPresentationIfExists(){
        // si la presentacion no se asoció a un id, reviso si no se puede asociar a otros case insensitive
        if ($scope.form.product.presentation != null && $scope.form.product.presentation.id == null) {
            angular.forEach($scope.form.presentations, function(presentation) {
                //si los nombres son iguales en lower case entonces es la misma pero con otro case
                if (presentation.name.toLowerCase() == $scope.form.product.presentation.name.toLowerCase()) {
                    $scope.form.product.presentation.name = presentation.name;
                    $scope.form.product.presentation.id = presentation.id;
                }
            });
        }
    };

    function matchDrugIfExists(){
        // si la droga no se asoció a un id, reviso si no se puede asociar a otros case insensitive
        if ($scope.form.product.drug != null && $scope.form.product.drug.id == null) {
            angular.forEach($scope.form.drugs, function(drug) {
                //si los nombres son iguales en lower case entonces es la misma pero con otro case
                if (drug.name.toLowerCase() == $scope.form.product.drug.name.toLowerCase()) {
                    $scope.form.product.drug.name = drug.name;
                    $scope.form.product.drug.id = drug.id;
                }
            });
        }
    };

    $scope.refreshTableData = function() {
        ProductService.getList().then(function(response) {
            $scope.products = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de productos', 'get', 'error', 'female'), 'danger');
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
            iva: 21,
            utility: 0,
            unitPrice: 0,
            minimumStock: 0
        };
    };

    $scope.refreshFormData = function() {
        var request = ProductService.getById($routeParams.id);

        request.success = function(response) {
            $scope.form.product = response.plain();
        };
        request.error = function(response) {
            if (response.data != null && response.data.message != null){
                MessageService.message(response.data.message,'danger');
            } else {
                MessageService.message(MessageService.text('producto', 'get', 'error', 'male'), 'danger');
            }

            $location.path('products');
        };

        request.then(request.success, request.error);
    };

    $scope.refreshFormDropdownsData = function() {
        CategoryService.getList().then(function(response) {
            $scope.form.categories = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de categorias', 'get', 'error', 'female'), 'danger');
        });

        DrugService.getList().then(function(response) {
            $scope.form.drugs = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de drogas', 'get', 'error', 'female'), 'danger');
        });

        ManufacturerService.getList().then(function(response) {
            $scope.form.manufacturers = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de laboratorios', 'get', 'error', 'female'), 'danger');
        });

        MeasureUnitService.getList().then(function(response) {
            $scope.form.measureUnits = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de unidades de medida', 'get', 'error', 'female'), 'danger');
        });

        PresentationService.getList().then(function(response) {
            $scope.form.presentations = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de presentaciones', 'get', 'error', 'female'), 'danger');
        });

        SupplierService.getList().then(function(response) {
            $scope.form.suppliers = response.plain();
        }, function(response){
            MessageService.message(MessageService.text('lista de proveedores', 'get', 'error', 'female'), 'danger');
        });

        $scope.form.ivas = ProductService.getIvaOptions();
    };

    $scope.addFormDropdownValue = function(attribute) {
        if ($scope.form.product[attribute].id == null) {
            $scope.form.product[attribute] = {id: null, name: $scope.form.product[attribute]};
        }
    };
    
    $scope.calculateUtility = function() {
        $scope.form.product.utility = ProductService.calculateUtility($scope.form.product.cost, $scope.form.product.unitPrice, $scope.form.product.iva);
    };

    $scope.calculateUnitPrice = function() {
        $scope.form.product.unitPrice = ProductService.calculateUnitPrice($scope.form.product.cost, $scope.form.product.utility, $scope.form.product.iva);
    };

    $scope.formValidation = function(form) {
        angular.forEach(form, function(object) {
            if (angular.isObject(object) && angular.isDefined(object.$setDirty)) {
                object.$setDirty();
            }
        });

        return form.$invalid;
    };

    // filtro personalizado para productos:
    // - true: si el producto cumple con los criterios de busqueda
    // - false: en caso contrario
   $scope.customProductFilter = function(product){
        // checkeo que el criterio de busqueda no es undefined or null or '' or '     '
        if (!$scope.table.search || !$scope.table.search.trim()) {
            return true;    
        }

        // previo a comparar el criterio con el nombre y droga del producto 
        // coloco a todos en lowerCase para que la busqueda sea case insensitive
        var searchCriteria = $scope.table.search.toLowerCase();
        var productName = product.name.toLowerCase();
        var productDrugName = null;
        // verifico que el DrugName no sea undefined or null or '' or '      '
        if (!!product.drug && !!product.drug.name && !!product.drug.name.trim()) {
            productDrugName = product.drug.name.toLowerCase();
        }

        if (productName.indexOf(searchCriteria) != -1 || (productDrugName != null && productDrugName.indexOf(searchCriteria) != -1)){
            criteriaMatches = true;
        } else {
            criteriaMatches = false;
        }
        
        return criteriaMatches;
    };

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
