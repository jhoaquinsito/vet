app.factory('BatchService', function() {
    var updatedProducts = [];

    // funcion que devuelve la lista de productos actualizados pendientes de confirmar para actualizar stock
    // (puede ser una lista vacía o pueden haber quedado algunos en memoria si el usuario estaba cargando y no confirmo una carga)
    this.getUpdatedProducts = function() {
        return updatedProducts;
    };


    // funcion que agrega un producto a la lista de productos actualizados pendientes de confirmar para actualizar stock
    this.setUpdatedProduct = function(updatedProduct) {
        console.log(updatedProduct);

        // intento obtener el indice del producto actualizado en la lista de productos actualizados
        var updatedProductIndex = arrayGetIndexOfId(updatedProducts, updatedProduct.id);

        // si existe en la lista
        if (updatedProductIndex > -1){
            //lo actualizo
            updatedProducts[updatedProductIndex] = updatedProduct;
        } else { // si no existe en la lista
            //lo agrego
            updatedProducts.push(updatedProduct);
        }
    };

    // funcion que elimina un producto de la lista de productos actualizados a partir de su id
    this.removeUpdatedProduct = function(id) {
        // intento obtener el indice del producto a eliminar en la lista de productos actualizados
        var updatedProductIndex = arrayGetIndexOfId(updatedProducts, id);

        // si existe en la lista
        if (updatedProductIndex > -1){
            // lo borro de la lista
            updatedProducts.splice(updatedProductIndex,1);
        } else { // si no existe en la lista
            // TODO error
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

    // funcion que limpia la lista completa de productos
    this.clearListOfUpdatedProducts = function() {
        updatedProducts = [];
    }

    return this;
});
