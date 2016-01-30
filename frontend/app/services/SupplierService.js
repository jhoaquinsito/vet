app.factory('SupplierService', function(Restangular) {
    var service = Restangular.service('supplier');
    var servicePerson = Restangular.service('person');
    var serviceLegalPerson = Restangular.service('legalperson');

    this.getById = function(supplierId) {
        return serviceLegalPerson.one(supplierId).get();
    };

    this.getList = function() {
        return service.getList();
    };

    this.save = function(supplier) {
        supplier.client = false;
        supplier.active = true;
       
        return serviceLegalPerson.post(supplier);
    };

    this.remove = function(supplierId) {
        return servicePerson.one(supplierId).remove();
    };

    return this;
});
