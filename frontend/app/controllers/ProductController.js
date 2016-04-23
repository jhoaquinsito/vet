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
                MessageService.message(MessageService.text('producto', 'remove', 'error', 'male'), 'danger');
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

        var request = ProductService.save($scope.form.product);

        request.success = function(response) {
            MessageService.message(MessageService.text('producto', $routeParams.id == null ? 'add' : 'edit', 'success', 'male'), 'success');

            $location.path('products');
        };
        request.error = function(response) {
            MessageService.message(MessageService.text('producto', $routeParams.id == null ? 'add' : 'edit', 'error', 'male'), 'danger');

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

    $scope.init();
});
