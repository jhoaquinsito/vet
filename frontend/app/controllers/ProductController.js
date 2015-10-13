app.controller('ProductController', function($scope, $location, $rootScope, $route, $routeParams, CategoryService, DrugService, ManufacturerService, PresentationService, ProductService, MessageService, config) {
    $scope.name = 'Productos';
    $scope.action = $route.current.action;
    $scope.products = [];
    $scope.table = {};
    $scope.form = {};

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
        }
    };

    $scope.listProductsAction = function() {
        $rootScope.setTitle($scope.name, 'Listado de productos');

        $scope.table.pageSize = config.TABLE_PAGE_SIZE;

        $scope.refreshTableData();
    };

    $scope.addProductAction = function() {
        $rootScope.setTitle($scope.name, 'Agregar producto');

        $scope.refreshFormDropdownsData();
    };

    $scope.editProductAction = function() {
        $rootScope.setTitle($scope.name, 'Editar producto');

        $scope.refreshFormData();
        $scope.refreshFormDropdownsData();
    };

    $scope.removeProductAction = function(productId) {
        MessageService.confirm(MessageService.text('producto', 'remove', 'confirm', 'male')).then(function() {
            var request = ProductService.one(productId).remove();

            request.success = function(response) {
                $scope.refreshTableData();

                MessageService.message(MessageService.text('producto', 'remove', 'success', 'male'), 'success');
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

        var request = ProductService.post($scope.form.product);

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
            provider: null,
            cost: null,
            utility: null,
            unitPrice: null,
            minimumStock: null
        };
    };

    $scope.refreshFormData = function() {
        var request = ProductService.one($routeParams.id).get();

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

        PresentationService.getList().then(function(response) {
            $scope.form.presentations = response.plain();
        });
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

    $scope.init();
});
