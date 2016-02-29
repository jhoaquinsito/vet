app.factory('BarcodeService', function($http, Restangular, config) {
    var service = Restangular.service('printbatch');

    this.printbatch = function(form) {
        var barcodeBatch = {
            "productId": form.product.id,
            "isoDueDate": form.isoDueDate,
            "quantity": form.quantity
        };
        var url = config.API_BASE_URL + '/printbatch';
        return $http.post(url, barcodeBatch);
    };

    return this;
});
