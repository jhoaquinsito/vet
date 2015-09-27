package backend.product.batch;

import java.math.BigDecimal;
import java.util.Date;

import backend.product.Product;

public class BatchDTO {

	private Long iId;
	private BigDecimal iStock;
	private Integer iIsoDueDate;
	private Product iProduct;
	
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
	public Product getProduct() {
		return iProduct;
	}
	public void setProduct(Product pProduct) {
		this.iProduct = pProduct;
	}
}
