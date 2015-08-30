app.controller('ProductController', function($scope, $route, $routeParams, ProductService) {
  $scope.action = $route.current.action;
  $scope.id = $routeParams.id;

  $scope.init = function() {
    switch ($scope.action) {
      case 'list':
        $scope.listAction();
        break;
      case 'add':
        break;
      case 'edit':
        break;
    }
  };

  $scope.listAction = function() {
    var request = ProductService.elements();
  };

  $scope.init();
});
