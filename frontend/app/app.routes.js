app.config(function($routeProvider) {
  //productos
  $routeProvider.when('/products', {
    controller: 'ProductController',
    templateUrl: 'app/views/product/table-view.html'
  });

  $routeProvider.when('/products/add', {
    controller: 'ProductController',
    templateUrl: 'app/views/product/form-view.html'
  });

  $routeProvider.when('/products/edit/:id', {
    controller: 'ProductController',
    templateUrl: 'app/views/product/form-view.html'
  });

  //ruta no encontrada
  $routeProvider.otherwise({redirectTo: '/'});
});
