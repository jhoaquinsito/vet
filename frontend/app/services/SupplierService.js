app.factory('ProviderService', function(Restangular) {
    var service = Restangular.service('supplier');

    this.getById = function(supplierId) {
        return service.one(supplierId).get();
    };

    this.getList = function() {
        return service.getList();
    };

    this.save = function(supplier) {
        return service.post(supplier);
    };
    
    this.remove = function(supplierId) {
        return service.one(supplierId).remove();
    };
    
    return this;
});
