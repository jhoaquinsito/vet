app.factory('BatchService', function() {
    var updatedProducts = [];

    // funcion que devuelve la lista de productos actualizados pendientes de confirmar para actualizar stock
    // (puede ser una lista vacía o pueden haber quedado algunos en memoria si el usuario estaba cargando y no confirmo una carga)
    this.getUpdatedProducts = function() {
        return updatedProducts;
    };

    // funcion que devuelve un producto actualizado de la lista de productos por id
    this.getUpdatedProductById = function(updatedProductId) {
        // intento obtener el indice del producto actualizado en la lista de productos actualizados
        var updatedProductIndex = arrayGetIndexOfId(updatedProducts, updatedProductId);

        var updatedProduct = null;

        // si existe en la lista
        if (updatedProductIndex > -1){
            updatedProduct = updatedProducts[updatedProductIndex];
        } 

        return updatedProduct;        
    }

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

    // funcion que elimina un lote del la lista de lotes de un producto
    // devuelve el producto sin el lote
    this.removeBatchFromProductBatches = function(product, batchToBeRemoved){
        product.batches.forEach(function(item, key) {
            if (item == batchToBeRemoved) {
                product.batches.splice(key, 1);
            }
        });

        return product;
    };

    // funcion que actualiza la lista de lotes en la pantalla Cargar lotes con el lote nuevo/modificado
    this.updateListWithBatch = function(listOfBatches, batch) {
        if (batch.id != null) { // si es id del batch no es null/undefined
            // busco batch por id (si no existe devuelve -1 )
            var targetBatchIndex = getBatchIndexById(listOfBatches, batch.id); 

            if (targetBatchIndex > -1){
                if (listOfBatches[targetBatchIndex].isoDueDate == batch.isoDueDate){
                    // si la fecha es igual entonces actualizo el stock y no necesito hacer mas nada
                    listOfBatches[targetBatchIndex].stock = batch.stock;
                    return listOfBatches;
                } else {
                    // si la fecha es diferente, borrar el lote de la lista
                    listOfBatches.splice(targetBatchIndex, 1);
                }
            } else {
                console.log("Error: el identificador del batch no existe en la lista de batches del producto.");
            }        
        } 

        // busco batch por isoduedate (si no existe devuelve -1 )
        targetBatchIndex = getBatchIndexByIsoDueDate(listOfBatches, batch.isoDueDate);

        if (targetBatchIndex > -1){ // si existe ya un batch con esa isoduedate
            // actualizo el batch con los nuevos valores
            listOfBatches[targetBatchIndex].isoDueDate = batch.isoDueDate;
            listOfBatches[targetBatchIndex].stock = batch.stock;
        }
        else { // si no existe un batch con esa isoduedate
            // agrego el nuevo batch a la lista
            listOfBatches.push(batch);
        }

        return listOfBatches;
    };

    // funcion que busca un lote por fecha de vencimiento en una lista de lotes
    function getBatchIndexByIsoDueDate(listOfBatches, batchIsoDueDate) {
        for (var i = 0; i < listOfBatches.length; i++) {
            if (listOfBatches[i].isoDueDate === batchIsoDueDate) {
                return i;
            }
        }
        return -1;
    };

    // funcion que busca un lote por id en una lista de lotes
    function getBatchIndexById(listOfBatches, batchId) {
        for (var i = 0; i < listOfBatches.length; i++) {
            if (listOfBatches[i].id === batchId) {
                return i;
            }
        }
        return -1;
    };

    return this;
});
