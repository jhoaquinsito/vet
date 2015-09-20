package backend.product.presentation;

public class PresentationDTO {

	private Long iId;
	private String iName;

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

}