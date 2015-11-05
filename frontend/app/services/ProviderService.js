app.factory('ProviderService', function(Restangular) {
    var service = Restangular.service('provider');

    this.getById = function(providerId) {
        return service.one(providerId).get();
    };

    this.getList = function() {
        return service.getList();
    };

    this.save = function(provider) {
        return service.post(provider);
    };

    return this;
});
