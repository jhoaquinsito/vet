package backend.product.batch;

/**
 *  Un <code>BatchPrintDTO</code> es una representación de un lote que va a ser impreso
 *  su codigo de barras.
 *  Un lote tiene: un <strong>Id</strong>y una cantidad de repeticiones a imprimir: <strong>Quantity</strong>,
 * @author gonzalo
 *
 */
public class BatchPrintDTO {
	private Long iId;
	private int iQuantity ;
	
	public Long getId() {
		return iId;
	}
	public void setId(Long pId) {
		this.iId = pId;
	}
	public int getQuantity() {
		return iQuantity;
	}
	public void setQuantity(int pQuantity) {
		this.iQuantity = pQuantity;
	}
}
