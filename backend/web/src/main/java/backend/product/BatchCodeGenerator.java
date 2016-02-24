package backend.product;

/**
 * La clase abstracta BatchCodeGenerator encapsula la lógica de codificación y
 * decodificación de códigos de para los lotes de los productos.
 * @author lautaro
 *
 */
public abstract class BatchCodeGenerator {

	public static final int BASECODE = 10000;
	public static final String NULLBATCHID = "00000000";
	
	/**
	 * Este método devuelve el código de un loque que se usará para generar el código de
	 * barras.
	 * @param pProductId el identificador del producto al que pertenece el lote.
	 * @param pBatchISODueDate la fecha de vencimiento de los productos que componen el
	 * lote.
	 * @return mBatchCode el código del lote.
	 */
	public static String code(Long pProductId, Integer pBatchISODueDate){
		
		String mBatchCode = "";
		
		if(pBatchISODueDate == null){
			Long mProductCode = pProductId + BatchCodeGenerator.BASECODE;
			mBatchCode = mProductCode.toString() + BatchCodeGenerator.NULLBATCHID;
		} else{
			Long mProductCode = pProductId + BatchCodeGenerator.BASECODE;
			mBatchCode = mProductCode.toString() + pBatchISODueDate.toString();
		}
		
		return mBatchCode;
	}
	
	/**
	 * Este método permite obtener el identificador de producto asociado al código de lote.
	 * @param pBatchCode el código de lote del que se quiere extraer el identificador de 
	 * producto.
	 */
	public static Long getProductId(String pBatchCode){
		
		Long mProductCode = Long.valueOf(pBatchCode.substring(0, (pBatchCode.length() - 8)));
		
		return mProductCode - BatchCodeGenerator.BASECODE;
	}
	
	/**
	 * Este método permite obtener la fecha de vencimiento en formato ISO correspondiente
	 * al lote asociado al código de lote.
	 * @param pBatchCode el código de lote del cual se quiere extraer la fecha de
	 * vencimiento.
	 * @return mBatchISODueDate la fecha de vencimiento del lote.
	 */
	public static Integer getBatchISODueDate(String pBatchCode){
		
		Integer mBatchISODueDate = Integer.valueOf(pBatchCode.substring(pBatchCode.length() - 8));
		
		return mBatchISODueDate;
	}
	
}
