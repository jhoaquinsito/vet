app.controller('BarcodeController', function($scope, $location, $rootScope, $route, $routeParams, $filter, ProductService, BarcodeService, MessageService) {
    $scope.name = 'Productos';
    $scope.action = $route.current.action;
    $scope.form = {};

    $scope.init = function() {
        switch ($scope.action) {
            case 'barcode.printing':
                $scope.printingBarcodeAction();
                break;
        }
    };

    $scope.printingBarcodeAction = function() {
        $rootScope.setTitle($scope.name, 'Imprimir código de barra');

        $scope.refreshFormDropdownsData();
    };

    $scope.printBarcodeAction = function(form) {
        if ($scope.formValidation(form)) {
            return null;
        }
        MessageService.confirm(MessageService.text('código de barra', 'print', 'confirm', 'male')).then(function() {
            var request = BarcodeService.printbatch($scope.form);

            request.success = function(response) {
                MessageService.message(MessageService.text('código de barra', 'print', 'success', 'male'), 'success');

                $location.path('products');
            };
            request.error = function(response) {
                MessageService.message(MessageService.text('código de barra', 'print', 'error', 'male'), 'danger');

                $location.path('products');
            };

            request.then(request.success, request.error);
        });


    };

    $scope.refreshFormDropdownsData = function() {
        ProductService.getList().then(function(response) {
            $scope.form.products = response.plain();
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
