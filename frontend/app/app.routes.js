app.config(function($routeProvider) {
    //dashboard
    $routeProvider.when('/dashboard', {
        controller: 'DashboardController',
        templateUrl: 'app/views/dashboard/show-view.html',
        action: 'dashboard.show'
    });

    //productos
    $routeProvider.when('/products', {
        controller: 'ProductController',
        templateUrl: 'app/views/product/table-view.html',
        action: 'product.list'
    });

    $routeProvider.when('/products/add', {
        controller: 'ProductController',
        templateUrl: 'app/views/product/form-view.html',
        action: 'product.add'
    });

    $routeProvider.when('/products/edit/:id', {
        controller: 'ProductController',
        templateUrl: 'app/views/product/form-view.html',
        action: 'product.edit'
    });

    $routeProvider.when('/products/detail/:id', {
        controller: 'ProductController',
        templateUrl: 'app/views/product/detail-view.html',
        action: 'product.detail'
    });

    $routeProvider.when('/products/stock', {
        controller: 'ProductController',
        templateUrl: 'app/views/product/stock-view.html',
        action: 'product.stock'
    });

    //proveedores
    $routeProvider.when('/providers', {
        controller: 'ProviderController',
        templateUrl: 'app/views/provider/table-view.html',
        action: 'provider.list'
    });

    $routeProvider.when('/providers/add', {
        controller: 'ProviderController',
        templateUrl: 'app/views/provider/form-view.html',
        action: 'provider.add'
    });

    //ruta no encontrada
    $routeProvider.otherwise({redirectTo: '/dashboard'});
});
