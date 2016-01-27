app.factory('SupplierService', function(Restangular) {
    var service = Restangular.service('supplier');
    var serviceLegalPerson = Restangular.service('legalperson');

    this.getById = function(supplierId) {
        return serviceLegalPerson.one(supplierId).get();
    };

    this.getList = function() {
        return service.getList();
    };

    this.save = function(supplier) {
        return serviceLegalPerson.post(supplier);
    };

    this.remove = function(supplierId) {
        return service.one(supplierId).remove();
    };

    return this;
});
