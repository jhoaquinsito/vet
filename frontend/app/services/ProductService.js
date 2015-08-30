app.service('ProductService', function($http, config) {
  this.elements = function() {
    return $http.get(config.API_URL + '/productList');
  };
});
