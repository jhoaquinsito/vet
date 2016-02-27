package backend.product.batch;

/**
 *  Un <code>BatchPrintDTO</code> es una representación de un lote que va a ser impreso
 *  su codigo de barras.
 *  Un lote tiene: un <strong>Product Id</strong>, 
 *  una <strong>iIsoDueDate</strong>,
 *  y una cantidad de repeticiones a imprimir: <strong>Quantity</strong>,
 * @author gonzalo
 *
 */
public class BatchPrintDTO {
	private Long iProductId;
	 
	private int iQuantity ;
	
	private Integer iIsoDueDate;
	
	public Long getProductId() {
		return iProductId;
	}
	public void setProductId(Long pProductId) {
		this.iProductId = pProductId;
	}
	public int getQuantity() {
		return iQuantity;
	}
	public void setQuantity(int pQuantity) {
		this.iQuantity = pQuantity;
	}
	public Integer getIsoDueDate() {
		return iIsoDueDate;
	}
	public void setIsoDueDate(Integer pIsoDueDate) {
		this.iIsoDueDate = pIsoDueDate;
	}
}
