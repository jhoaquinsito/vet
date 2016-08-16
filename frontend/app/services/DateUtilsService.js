app.factory('DateUtilsService', function($filter) {

    // funcion que transforma un integer ISO del formato yyyyMMdd a un string yyyy/MM/dd
    this.isoDateToFormattedString = function(isoDate) {
        var formattedString = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
        
            // regex para formatear la fecha
            var pattern = /(\d{4})(\d{2})(\d{2})/;
            
            // aplico la regex para formatear la fecha al formato ISO 8601: 'yyyy-MM-dd'
            formattedString = isoDateString.replace(pattern, '$3/$2/$1');
        }

        return formattedString;
    }

    // funcion que pasa un objeto Date a un integer ISO
    this.dateObjectToIsoDate = function(dateObject){
        var stringDate = $filter('date')(dateObject, 'yyyyMMdd');
        var isoDate = null;
        // verifico que no sea un NaN (illegal number)
        if (!isNaN(parseInt(stringDate))){
            isoDate = parseInt(stringDate);
        }
        return isoDate;
    }

    // funcion que transforma un integer ISO del formato yyyyMMdd a un objeto Javascript Date
    this.isoDateToDateObject = function(isoDate){
        var dateObject = null;

        if (isoDate != null){
            var isoDateString = isoDate.toString();
            
            var year = isoDateString.substr(0,4);
            var month = isoDateString.substr(4,2);
            var day = isoDateString.substr(6,2);
            
            dateObject = new Date(year,month-1, day); 
        }

        return dateObject;
    }

    return this;
});
