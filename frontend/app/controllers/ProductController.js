app.controller('ProductController', function($scope, $rootScope, $route, CategoryService, DrugService, ManufacturerService, PresentationService, ProductService) {
    $scope.name = 'Productos';
    $scope.action = $route.current.action;
    $scope.products = [];
    $scope.form = {};

    $scope.init = function() {
        switch ($scope.action) {
            case 'list':
                $scope.listProductsAction();
                break;
            case 'add':
                $scope.addProductAction();
                break;
            case 'edit':
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
        $scope.resetFormData();
    };

    $scope.editProductAction = function() {
        $rootScope.setTitle($scope.name, 'Editar producto');
    };

    $scope.saveProductAction = function(form) {
        if (form.$invalid) {
            return null;
        }

        var request = ProductService.post($scope.form.product);

        request.success = function(response) {
            $scope.resetFormData();

            alert('Producto cargado con éxito.');
        };
        request.error = function(response) {
            alert('No se pudo cargar el producto.');
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

    $scope.refreshFormDropdownsData = function() {
        CategoryService.getList().then(function(response) {
            $scope.form.categories = response;
        });

        DrugService.getList().then(function(response) {
            $scope.form.drugs = response;
        });

        ManufacturerService.getList().then(function(response) {
            $scope.form.manufacturers = response;
        });

        PresentationService.getList().then(function(response) {
            $scope.form.presentations = response;
        });
    };

    $scope.init();
});
