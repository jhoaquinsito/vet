package backend.product.measure_unit;

public class MeasureUnitDTO {

	private Long iId;
	private String iName;
	private String iAbbreviation;

	public Long getId() {
		return iId;
	}

	public void setId(Long iId) {
		this.iId = iId;
	}

	public String getName() {
		return iName;
	}

	public void setName(String iName) {
		this.iName = iName;
	}

	public String getAbbreviation() {
		return iAbbreviation;
	}

	public void setAbbreviation(String pAbbreviation) {
		this.iAbbreviation = pAbbreviation;
	}
}