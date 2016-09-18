app.factory('FormOfSaleService', function(Restangular) {
    var service = Restangular.service('formofsale');

    this.getList = function() {
        return service.getList();
    };

    return this;
});