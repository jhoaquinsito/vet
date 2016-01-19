app.factory('ClientService', function(Restangular) {
    var service = Restangular.service('client');

    this.getById = function(clientId) {
        return service.one(clientId).get();
    };

    this.getList = function() {
//        var client = new Object();
//        client.id = 1;
//        client.name = "Ema";
//        client.cuit = "123456";
//        client.direccion = "Alvares 124";
//        client.celular = "15487963";
//        var clientes = new Array();
//        clientes[0] = client;
//        return clients;
        return service.getList();
    };

    this.save = function(client) {
        return service.post(client);
    };
    
    this.remove = function(clientId) {
        return service.one(clientId).remove();
    };
    
    return this;
});
