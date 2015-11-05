app.factory('MeasureUnitService', function(Restangular) {
    var service = Restangular.service('measure_unit');

    this.getList = function() {
        return service.getList();
    };

    return this;
});
