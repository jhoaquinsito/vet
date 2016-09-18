package backend.form_of_sale;

public class FormOfSaleForSalesReportDTO {
	private Long iId;
	private String iDescription;
	private Integer iBeginDate;
	private Integer iEndDate;
	
	public Long getId() {
		return iId;
	}
	public void setId(Long pId) {
		this.iId = pId;
	}
	public String getDescription() {
		return iDescription;
	}
	public void setDescription(String pDescription) {
		this.iDescription = pDescription;
	}
	public Integer getBeginDate() {
		return iBeginDate;
	}
	public void setBeginDate(Integer pBeginDate) {
		this.iBeginDate = pBeginDate;
	}
	public Integer getEndDate() {
		return iEndDate;
	}
	public void setEndDate(Integer pEndDate) {
		this.iEndDate = pEndDate;
	}
}
