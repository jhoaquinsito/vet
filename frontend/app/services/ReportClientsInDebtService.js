app.factory('ReportClientsInDebtService', function($http, config) {

    this.getList = function() {
        return $http.get(config.API_BASE_URL +'/report/person-balance');
    };

    return this;
});
