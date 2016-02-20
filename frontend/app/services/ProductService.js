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

    this.calculateUtility = function(cost, unitPrice) {
        return Math.round((unitPrice * 100 / cost - 100) * 100) / 100;
    };

    this.calculateUnitPrice = function(cost, utility) {
        return Math.round((cost * (utility + 100) / 100) * 100) / 100;
    };

    this.getIvaOptions = function() {
        var ivas = [
            {name: 'Sin IVA', value: 0},
            {name: '10.5 %', value: 10.5},
            {name: '21 %', value: 21}
        ];

        return ivas;
    };

    return this;
});
