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

    $routeProvider.when('/products/stock', {
        controller: 'ProductController',
        templateUrl: 'app/views/product/stock-view.html',
        action: 'product.stock'
    });

    //ruta no encontrada
    $routeProvider.otherwise({redirectTo: '/dashboard'});
});
