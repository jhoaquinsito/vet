app.service('MessageService', function(ngToast) {
    this.articles = {
        female: 'la',
        male: 'el'
    };

    this.verbs = {
        add: {success: 'agregó', error: 'agregar'},
        edit: {success: 'editó', error: 'editar'},
        remove: {success: 'borró', error: 'borrar'}
    };

    this.texts = {
        success: '{article} {entity} se {verb} correctamente',
        error: 'ocurrió un error al intentar {verb} {article} {entity}'
    };

    this.getText = function(entity, action, type, gender) {
        var article = this.articles[gender];
        var verb = this.verbs[action][type];
        var text = this.texts[type];

        text = text.replace('{article}', article);
        text = text.replace('{verb}', verb);
        text = text.replace('{entity}', entity);

        return this.capitalize(text);
    };

    this.capitalize = function(text) {
        return text.charAt(0).toUpperCase() + text.slice(1);
    };

    this.genericMessage = function(entity, action, type, gender) {
        var text = this.getText(entity, action, type, gender);

        switch (type) {
            case 'error':
                type = 'danger';
                break;
        }

        this.message(text, type);
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

        ngToast.create({
            className: type,
            content: '<i class="fa fa-' + iconName + '"></i>' + text,
            dismissButton: true,
            timeout: 5000
        });
    };
});
