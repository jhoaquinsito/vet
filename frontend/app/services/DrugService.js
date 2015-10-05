app.factory('DrugService', function($resource, config) {
    return $resource(config.API_BASE_URL + '/drug/:id');
});
