package backend.person.children.natural_person;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.PersonDTO;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
