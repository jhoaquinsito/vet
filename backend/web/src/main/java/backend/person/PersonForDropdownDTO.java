package backend.person;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import backend.person.settlement.SettlementDTO;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PersonForDropdownDTO  {

	private Long iId;
	private String iName;
	private Set<SettlementDTO> iSettlements;
	
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
	public Set<SettlementDTO> getSettlements() {
		return iSettlements;
	}
	public void setSettlements(Set<SettlementDTO> pSettlements) {
		this.iSettlements = pSettlements;
	}

}