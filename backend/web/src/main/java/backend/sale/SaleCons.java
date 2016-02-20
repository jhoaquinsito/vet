package backend.sale;

public class SaleCons {
	public static final String cDELETED_SALE_EXCEPTION_MESSAGE 			
							= "Intentaste obtener una venta eliminada lógicamente.";
	public static final String cSALE_DOESNT_EXIST_EXCEPTION_MESSAGE 
							= "Intentaste obtener una venta que no existe.";
	public static final String cSALE_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE 
							= "Hubo un problema con alguna de las restricciones de la base de datos. Muy probablemente el nombre de un venta, o alguno de sus hijos, violó una restricción unique.";
	public static final String cCANNOT_SAVE_SALE_EXCEPTION_MESSAGE 
							= "La venta que intentas guardar no se puede guardar: o no existe o está eliminada lógicamente.";
	public static final String cSALE_NULL_EXCEPTION_MESSAGE 
							= "La venta no tiene valores válidos";
	public static final String cSALE_SETTLEMENT_NULL_EXCEPTION_MESSAGE 
							= "La Venta que desea registrar no contiene un Pago válido";
	
	public static final String cCANNOT_SAVE_WITHOUT_SALELINE_EXCEPTION_MESSAGE
							= "La venta no puede guardarse sin tener ningún ítem vendido asignado.";
	public static final String cCANNOT_SAVE_WITHOUT_SALELINE_WITHOUT_PRODUCT_EXCEPTION_MESSAGE
	= "La venta no puede guardarse sin tener ningún ítem vendido asignado que a su vez no tenga un producto asociado.";
	
	public static final String cCANNOT_GET_SALES = "Error al recuperar las ventas.";
	
}
