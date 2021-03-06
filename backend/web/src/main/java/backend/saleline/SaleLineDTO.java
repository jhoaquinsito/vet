package backend.saleline;

import backend.product.batch.BatchDTO;

public class SaleLineDTO {

	private Long iId;

	private float iQuantity ;

	private float iUnit_Price;
	
	private float iDiscount;
		
	private BatchDTO iBatch;
	
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

	public BatchDTO getBatch() {
		return iBatch;
	}

	public void setBatch(BatchDTO pBatch) {
		this.iBatch = pBatch;
	}

	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
	}
	
}
