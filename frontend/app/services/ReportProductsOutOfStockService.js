app.factory('ReportProductsOutOfStockService', function($http, config) {

    this.getList = function() {
        return $http.get(config.API_BASE_URL +'/report/products-without-stock');
    };

    return this;
});
