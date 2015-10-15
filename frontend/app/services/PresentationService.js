app.factory('PresentationService', function(Restangular) {
    var service = Restangular.service('presentation');

    this.getList = function() {
        return service.getList();
    };

    return this;
});
