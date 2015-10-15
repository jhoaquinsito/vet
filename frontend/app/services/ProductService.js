app.factory('ProductService', function(Restangular) {
    var service = Restangular.service('product');

    this.getById = function(productId) {
        return service.one(productId).get();
    };

    this.getList = function() {
        return service.getList();
    };

    this.save = function(product) {
        return service.post(product);
    };

    this.remove = function(productId) {
        return service.one(productId).remove();
    };

    return this;
});
