package backend.person.iva_category;

import java.math.BigDecimal;

public class IVACategoryDTO  {

	private Long iId;
	private String iDescription;
	private BigDecimal iPercentage;
	
	public Long getId() {
		return iId;
	}
	public void setId(Long iId) {
		this.iId = iId;
	}
	public String getDescription() {
		return iDescription;
	}
	public void setDescription(String iDescription) {
		this.iDescription = iDescription;
	}
	public BigDecimal getPercentage() {
		return iPercentage;
	}
	public void setPercentage(BigDecimal iPercentage) {
		this.iPercentage = iPercentage;
	}

}

