app.factory('BatchService', function() {
    var updatedProducts = [];


    this.getUpdatedProducts = function() {
        return updatedProducts;
    };


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
