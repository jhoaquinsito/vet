app.factory('CategoryService', function(Restangular) {
    var service = Restangular.service('category');

    this.getList = function() {
        return service.getList();
    };

    return this;
});
