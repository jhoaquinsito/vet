app.factory('SaleService', function(Restangular) {
    var service = Restangular.service('sale');

    this.save = function(sale) {
        //valores por default
        sale.client = false;
        sale.active = true;

        return service.post(sale);
    };

    return this;
});
