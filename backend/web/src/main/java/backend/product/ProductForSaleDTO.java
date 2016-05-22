package backend.product;

import java.math.BigDecimal;

public class ProductForSaleDTO {

	private Long iId;
	private String iName;
	private BigDecimal iUnitPrice;
	private String iMeasureUnitAbbreviation;
	private Long iBatchId;
	private Integer iBatchIsoDueDate;
	
	public Long getId() {
		return iId;
	}
	public void setId(Long pId) {
		this.iId = pId;
	}
	public String getName() {
		return iName;
	}
	public void setName(String pName) {
		this.iName = pName;
	}
	public BigDecimal getUnitPrice() {
		return iUnitPrice;
	}
	public void setUnitPrice(BigDecimal pUnitPrice) {
		this.iUnitPrice = pUnitPrice;
	}
	public Long getBatchId() {
		return iBatchId;
	}
	public void setBatchId(Long pBatchId) {
		this.iBatchId = pBatchId;
	}
	public Integer getBatchIsoDueDate() {
		return iBatchIsoDueDate;
	}
	public void setBatchIsoDueDate(Integer pBatchIsoDueDate) {
		this.iBatchIsoDueDate = pBatchIsoDueDate;
	}
	public String getMeasureUnitAbbreviation() {
		return iMeasureUnitAbbreviation;
	}
	public void setMeasureUnitAbbreviation(String pMeasureUnitAbbreviation) {
		this.iMeasureUnitAbbreviation = pMeasureUnitAbbreviation;
	}

}
