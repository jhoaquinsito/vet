app.factory('DrugService', function(Restangular) {
    var service = Restangular.service('drug');

    this.getList = function() {
        return service.getList();
    };

    return this;
});
