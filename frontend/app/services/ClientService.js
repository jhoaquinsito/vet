app.factory('ClientService', function($http, Restangular, config) {
    var serviceClient = Restangular.service('client');
    var serviceLegalPerson = Restangular.service('legalperson');
    var serviceNaturalPerson = Restangular.service('naturalperson');

    this.getById = function(clientId) {
        return serviceClient.one(clientId).get();
    };

    this.getList = function() {
        return serviceClient.getList();
    };

    this.getListForDropdown = function() {
        return $http.get(config.API_BASE_URL + '/client/dropdown');
    }

    this.save = function(client) {
        if (client.cuit != null) {
            client.client = true;
            return serviceLegalPerson.post(client);
        } else {
            return serviceNaturalPerson.post(client);
        }
    };

    this.remove = function(clientId) {
        return serviceClient.one(clientId).remove();
    };

    this.getFullName = function(client){
        return (client.cuit ? client.name : client.name + ' ' +client.lastName);
    }

    return this;
});
