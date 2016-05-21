package backend.product.batch;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.product.ProductDTO;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BatchDTO {

	private Long iId;
	private BigDecimal iStock;
	private Integer iIsoDueDate;
	private ProductDTO iProduct;
	
	
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
	public ProductDTO getProduct() {
		return iProduct;
	}
	public void setProduct(ProductDTO pProduct) {
		this.iProduct = pProduct;
	}
	
	
}
