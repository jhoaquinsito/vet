app.factory('ManufacturerService', function(Restangular) {
    var service = Restangular.service('manufacturer');

    this.getList = function() {
        return service.getList();
    };

    return this;
});
