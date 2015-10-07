app.controller('ProductController', function($scope, $location, $rootScope, $route, $routeParams, CategoryService, DrugService, ManufacturerService, PresentationService, ProductService, MessageService) {
    $scope.name = 'Productos';
    $scope.action = $route.current.action;
    $scope.products = [];
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

    $scope.saveProductAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }

        var request = ProductService.post($scope.form.product);

        request.success = function(response) {
            MessageService.message('El producto se creó correctamente', 'success');

            $location.path('products');
        };
        request.error = function(response) {
            MessageService.message('Ocurrió un error al intentar crear el producto.', 'danger');

            $location.path('products');
        };

        request.then(request.success, request.error);
    };

    $scope.resetFormData = function() {
        $scope.form.product = {
            name: null,
            description: null,
            category: null,
            manufacturer: null,
            presentation: null,
            measureUnit: null,
            drugs: [],
            provider: null,
            cost: null,
            utility: null,
            unitPrice: null,
            minimumStock: null
        };
    };

    $scope.refreshFormData = function() {
        ProductService.one($routeParams.id).get().then(function(response) {
            $scope.form.product = response.plain();
        });
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
