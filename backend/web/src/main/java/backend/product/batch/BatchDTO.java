package backend.product.batch;

import java.math.BigDecimal;

public class BatchDTO {

	private Long iId;
	private BigDecimal iStock;
	private Integer iIsoDueDate;
	private Long iProduct;
	
	
	public Long getId() {
		return iId;
	}
	public void setId(Long pId) {
		this.iId = pId;
	}
	public BigDecimal getStock() {
		return iStock;
	}
	public void setStock(BigDecimal pStock) {
		this.iStock = pStock;
	}
	public Integer getIsoDueDate() {
		return iIsoDueDate;
	}
	public void setIsoDueDate(Integer pIsoDueDate) {
		this.iIsoDueDate = pIsoDueDate;
	}
	public Long getProduct() {
		return iProduct;
	}
	public void setProduct(Long pProduct) {
		this.iProduct = pProduct;
	}
	
	
}
