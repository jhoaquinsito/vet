package backend.person.city;

import backend.person.city.state.StateDTO;

public class CityDTO  {

	private Long iId;
	private String iName;
	private StateDTO iState;
	
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
	public StateDTO getState() {
		return iState;
	}
	public void setState(StateDTO pState) {
		this.iState = pState;
	}

}