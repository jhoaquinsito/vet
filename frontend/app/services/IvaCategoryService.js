app.factory('IvaCategoryService', function(Restangular) {
    var service = Restangular.service('IvaCategory');

    this.getList = function() {
        return service.getList();
    };

    return this;
});
