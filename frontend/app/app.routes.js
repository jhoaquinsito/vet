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
    $routeProvider.when('/suppliers', {
        controller: 'SupplierController',
        templateUrl: 'app/views/supplier/table-view.html',
        action: 'supplier.list'
    });

    $routeProvider.when('/suppliers/add', {
        controller: 'SupplierController',
        templateUrl: 'app/views/supplier/form-view.html',
        action: 'supplier.add'
    });

    $routeProvider.when('/suppliers/edit/:id', {
        controller: 'SupplierController',
        templateUrl: 'app/views/supplier/form-view.html',
        action: 'supplier.edit'
    });

    //ruta no encontrada
    $routeProvider.otherwise({redirectTo: '/dashboard'});
});
