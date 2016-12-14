app.service('MessageService', function($alert, $modal, $q) {
    this.articles = {
        female: 'la',
        male: 'el'
    };

    this.verbs = {
        add: {success: 'agregó', error: 'agregar'},
        edit: {success: 'editó', error: 'editar'},
        remove: {success: 'borró', error: 'borrar', confirm: 'borrar'},
        print: {success: 'imprimió', error: 'imprimir', confirm: 'imprimir'},
        get: {sucess: 'obtuvo', error: 'obtener'},
        report: {success: 'procesó', error: 'procesar'}
    };

    this.texts = {
        success: '{article} {entity} se {verb} correctamente',
        error: 'Ocurrió un error al intentar {verb} {article} {entity}',
        confirm: '¿Seguro desea {verb} {article} {entity}?'
    };

    this.capitalize = function(text) {
        return text.charAt(0).toUpperCase() + text.slice(1);
    };

    this.text = function(entity, action, type, gender) {
        var article = this.articles[gender];
        var verb = this.verbs[action][type];
        var text = this.texts[type];

        text = text.replace('{article}', article);
        text = text.replace('{verb}', verb);
        text = text.replace('{entity}', entity);

        return this.capitalize(text);
    };

    this.message = function(text, type) {
        var iconName;

        switch (type) {
            case 'success':
                iconName = 'check-circle';
                break;
            case 'warning':
                iconName = 'exclamation-circle';
                break;
            case 'danger':
                iconName = 'times-circle';
                break;
            case 'info':
                iconName = 'info-circle';
                break;
        }

        $alert({
            container: '#alert-wrapper',
            content: '<i class="fa fa-' + iconName + '"></i>' + text,
            type: type,
            duration: 5
        });
    };

    this.confirm = function(message) {
        var deferred = $q.defer();

        $modal({
            title: 'Confirmación',
            content: message,
            show: true,
            backdrop: 'static',
            keyboard: false,
            templateUrl: 'app/views/layout/confirm-popup-view.html',
            resolve: {},
            controller: function($scope) {
                $scope.accept = function() {
                    deferred.resolve();
                    $scope.$hide();
                };

                $scope.cancel = function() {
                    deferred.reject();
                    $scope.$hide();
                };
            }
        });

        return deferred.promise;
    };
});
