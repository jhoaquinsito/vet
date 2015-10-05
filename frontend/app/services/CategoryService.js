app.factory('CategoryService', function($resource, config) {
    return $resource(config.API_BASE_URL + '/category/:id');
});
