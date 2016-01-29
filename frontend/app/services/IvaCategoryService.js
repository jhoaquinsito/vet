app.factory('IvaCategoryService', function(Restangular) {
    var service = Restangular.service('ivacategory');

    this.getList = function() {
        return service.getList();
    };

    return this;
});
