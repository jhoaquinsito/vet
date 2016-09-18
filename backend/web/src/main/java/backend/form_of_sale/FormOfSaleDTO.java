package backend.form_of_sale;


public class FormOfSaleDTO {
	private Long iId;
	private String iDescription;
	
	
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
	
}
