app.factory('SaleService', function(Restangular) {
    var service = Restangular.service('sale');

    this.save = function(sale) {
        //valores por default
        sale.invoiced = false;
        sale.paied_out = true;

        return service.post(sale);
    };

    return this;
});
