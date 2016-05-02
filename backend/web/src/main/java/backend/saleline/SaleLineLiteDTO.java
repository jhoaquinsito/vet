package backend.saleline;

public class SaleLineLiteDTO {

	private Long iId;

	private float iQuantity ;

	private float iUnit_Price;
	
	private float iDiscount;
		
	private Long iProduct;
	
	private Long iBatch;
	
	private Integer iISODueDate;
	
	public float getQuantity() {
		return iQuantity;
	}

	public void setQuantity(float pQuantity) {
		this.iQuantity = pQuantity;
	}

	public float getUnit_Price() {
		return iUnit_Price;
	}

	public void setUnit_Price(float pUnit_Price) {
		this.iUnit_Price = pUnit_Price;
	}


	public float getDiscount() {
		return iDiscount;
	}

	public void setDiscount(float pDiscount) {
		this.iDiscount = pDiscount;
	}

	public Long getProduct() {
		return iProduct;
	}

	public void setProduct(Long pProduct) {
		this.iProduct = pProduct;
	}
	
	public Long getBatch() {
		return iBatch;
	}

	public void setBatch(Long pBatch) {
		this.iBatch = pBatch;
	}

	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
	}

	public Integer getISODueDate() {
		return iISODueDate;
	}

	public void setISODueDate(Integer pISODueDate) {
		this.iISODueDate = pISODueDate;
	}	
	
}
