app.factory('ProductService', function($resource, config) {
    return $resource(config.API_BASE_URL + '/product/:id');
});
