app.factory('ProductService', function($http, Restangular, config) {
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

    this.calculateUtility = function(cost, unitPrice, iva) {
        var costWithIva = Math.round((cost * (iva + 100) / 100) * 100) / 100;
        return Math.round((unitPrice * 100 / costWithIva - 100) * 100) / 100;
    };

    this.calculateUnitPrice = function(cost, utility, iva) {
        var costWithIva = Math.round((cost * (iva + 100) / 100) * 100) / 100;
        return Math.round((costWithIva * (utility + 100) / 100) * 100) / 100;
    };

    this.getIvaOptions = function() {
        var ivas = [
            {name: 'Sin IVA', value: 0},
            {name: '10.5 %', value: 10.5},
            {name: '21 %', value: 21}
        ];

        return ivas;
    };

    this.getByBatchCode = function(productBatchCode) {
        return $http.get(config.API_BASE_URL +'/product/bybatchcode/'+productBatchCode);
    };

    this.saveBunch = function(bunchOfProducts) {
        return $http.post(config.API_BASE_URL +'/product/bunch', bunchOfProducts);
    }

    this.calculateStock = function(product){
        var sum = 0;
        for (var i = 0; i < product.batches.length; i++) {
            sum = sum + product.batches[i].stock;
        } 
        return sum;
    }

    return this;
});
