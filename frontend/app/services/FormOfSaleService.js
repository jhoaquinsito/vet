app.factory('FormOfSaleService', function(Restangular) {
    var service = Restangular.service('formofsale');

    this.getList = function() {
        return service.getList();
    };

    this.getListForFinalConsumer = function(pFormOfSaleOptions){
    	return [this.selectCashOption(pFormOfSaleOptions)];
    };

    this.selectCashOption = function(pFormOfSaleOptions){
    	var mCashOptionIndex = arrayGetIndexOfId(pFormOfSaleOptions, 1);

    	if (mCashOptionIndex != -1){
    		return pFormOfSaleOptions[mCashOptionIndex];
    	}
    };

    // function utilitaria que obtiene el indice de un elemento de un array con un "id" dado (devuelve -1 si no lo encuentra)
    function arrayGetIndexOfId(array, id) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].id === id) {
                return i;
            }
        }
        return -1;
    };

    return this;
});