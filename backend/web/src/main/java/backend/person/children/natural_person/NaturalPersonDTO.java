package backend.person.children.natural_person;

import backend.person.PersonDTO;

public class NaturalPersonDTO extends PersonDTO {
		
	private Integer iNationalId;
	private String iLastName;
	
	public Integer getNationalId() {
		return iNationalId;
	}
	public void setNationalId(Integer pNationalId) {
		this.iNationalId = pNationalId;
	}
	public String getLastName() {
		return iLastName;
	}
	public void setLastName(String pLastName) {
		this.iLastName = pLastName;
	}

}
