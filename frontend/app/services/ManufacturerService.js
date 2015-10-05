app.factory('ManufacturerService', function($resource, config) {
    return $resource(config.API_BASE_URL + '/manufacturer/:id');
});
