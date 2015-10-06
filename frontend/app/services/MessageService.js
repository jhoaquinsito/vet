app.service('MessageService', function(ngToast) {
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
