package backend.person;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PersonForDropdownDTO  {

	private Long iId;
	private String iName;
	private String iLastName;
	
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
	public String getLastName() {
		return iLastName;
	}
	public void setLastName(String pLastName) {
		this.iLastName = pLastName;
	}

}