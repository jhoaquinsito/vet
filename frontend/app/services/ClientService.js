app.factory('ClientService', function(Restangular) {
    var serviceClient = Restangular.service('client');
    var serviceLegalPerson = Restangular.service('legalperson');
    var serviceNaturalPerson = Restangular.service('naturalperson');

    this.getById = function(clientId) {
        var Client = serviceNaturalPerson.one(clientId).get();
        if (typeof  Client.cuit === 'undefined')
            Client.clientType = "1";
        else
            Client.clientType = "2";

        return Client;
        //return serviceClient.one(clientId).get();
    };

    this.getList = function() {
        return serviceClient.getList();
    };

    this.save = function(client) {
        if (client.cuit !== null)
            return serviceLegalPerson.post(client);
        else
            return serviceNaturalPerson.post(client);
    };

    this.remove = function(clientId) {
        return service.one(clientId).remove();
    };

    return this;
});
