package backend.saleline;

import java.math.BigDecimal;

public class SaleLineForReportDTO {

	private Long iId;

	private float iQuantity;

	private float iUnit_Price;

	private float iDiscount;

	private Long iBatchId;

	private BigDecimal iBatchStock;
	
	private Integer iBatchIsoDueDate;
	
	private String iBatchProductName;
	
	private String iBatchProductMeasureUnitAbbreviation;
	
	private BigDecimal iBatchProductUnitPrice;
	

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

	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
	}

	public Long getBatchId() {
		return iBatchId;
	}

	public void setBatchId(Long pBatchId) {
		this.iBatchId = pBatchId;
	}

	public BigDecimal getBatchStock() {
		return iBatchStock;
	}

	public void setBatchStock(BigDecimal pBatchStock) {
		this.iBatchStock = pBatchStock;
	}

	public Integer getBatchIsoDueDate() {
		return iBatchIsoDueDate;
	}

	public void setBatchIsoDueDate(Integer pBatchIsoDueDate) {
		this.iBatchIsoDueDate = pBatchIsoDueDate;
	}

	public String getBatchProductName() {
		return iBatchProductName;
	}

	public void setBatchProductName(String pBatchProductName) {
		this.iBatchProductName = pBatchProductName;
	}

	public String getBatchProductMeasureUnitAbbreviation() {
		return iBatchProductMeasureUnitAbbreviation;
	}

	public void setBatchProductMeasureUnitAbbreviation(String pBatchProductMeasureUnitAbbreviation) {
		this.iBatchProductMeasureUnitAbbreviation = pBatchProductMeasureUnitAbbreviation;
	}

	public BigDecimal getBatchProductUnitPrice() {
		return iBatchProductUnitPrice;
	}

	public void setBatchProductUnitPrice(BigDecimal pBatchProductUnitPrice) {
		this.iBatchProductUnitPrice = pBatchProductUnitPrice;
	}

}
