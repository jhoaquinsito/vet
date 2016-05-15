app.config(function($routeProvider) {
    //código de barra
    $routeProvider.when('/barcode/printing', {
        controller: 'BarcodeController',
        templateUrl: 'app/views/barcode/form-view.html',
        action: 'barcode.printing'
    });
    
    //stock
    $routeProvider.when('/batches/add', {
        controller: 'BatchController',
        templateUrl: 'app/views/batch/form-view.html',
        action: 'batch.add'
    });

    $routeProvider.when('/batches/bulk_update', {
        controller: 'BatchController',
        templateUrl: 'app/views/batch/confirmation-view.html',
        action: 'batch.bulk_update'
    });

    //clientes
    $routeProvider.when('/clients', {
        controller: 'ClientController',
        templateUrl: 'app/views/client/table-view.html',
        action: 'client.list'
    });

    $routeProvider.when('/clients/add', {
        controller: 'ClientController',
        templateUrl: 'app/views/client/form-view.html',
        action: 'client.add'
    });

    $routeProvider.when('/clients/edit/:id', {
        controller: 'ClientController',
        templateUrl: 'app/views/client/form-view.html',
        action: 'client.edit'
    });

    $routeProvider.when('/clients/detail/:id', {
        controller: 'ClientController',
        templateUrl: 'app/views/client/detail-view.html',
        action: 'client.detail'
    });

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

    //ventas
    $routeProvider.when('/sales', {
        controller: 'SaleController',
        templateUrl: 'app/views/sale/form-view.html',
        action: 'sale.add'
    });

	  //pagos - cuenta corriente
    $routeProvider.when('/settlements', {
        controller: 'SettlementController',
        templateUrl: 'app/views/settlement/form-view.html',
        action: 'settlement.detail'
    });
	
    //ruta no encontrada
    $routeProvider.otherwise({redirectTo: '/dashboard'});
});
