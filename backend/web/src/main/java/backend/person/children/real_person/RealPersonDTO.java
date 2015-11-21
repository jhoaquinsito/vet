package backend.person.children.real_person;

import backend.person.PersonDTO;

public class RealPersonDTO extends PersonDTO {
		
	private Integer iDocumentId;
	private String iLastName;
	
	public Integer getDocumentId() {
		return iDocumentId;
	}
	public void setDocumentId(Integer pDocumentId) {
		this.iDocumentId = pDocumentId;
	}
	public String getLastName() {
		return iLastName;
	}
	public void setLastName(String pLastName) {
		this.iLastName = pLastName;
	}

}
