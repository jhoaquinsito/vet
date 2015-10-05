app.controller('ProductController', function($scope, $route, CategoryService, DrugService, ManufacturerService, PresentationService, ProductService) {
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

    };

    $scope.addProductAction = function() {
        $scope.refreshFormDropdownsData();
        $scope.resetFormData();
    };

    $scope.editProductAction = function() {

    };

    $scope.saveProductAction = function() {
        ProductService.save($scope.form.product);
    };

    $scope.resetFormData = function() {
        $scope.form.product = {
            name: null,
            description: null,
            category: {name: ''},
            manufacturer: {name: ''},
            presentation: {name: ''},
            measureUnit: {name: ''},
            drugs: [],
            provider: null,
            cost: null,
            utility: null,
            unitPrice: null,
            minimumStock: null
        };
    };

    $scope.refreshFormDropdownsData = function() {
        $scope.form.categories = CategoryService.query();
        $scope.form.drugs = DrugService.query();
        $scope.form.manufacturers = ManufacturerService.query();
        $scope.form.presentations = PresentationService.query();
    };

    $scope.init();
});
