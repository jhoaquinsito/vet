app.factory('PresentationService', function($resource, config) {
    return $resource(config.API_BASE_URL + '/presentation/:id');
});
