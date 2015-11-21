package backend.person.children.legal_person;

import java.math.BigDecimal;

import backend.person.PersonDTO;

public class LegalPersonDTO extends PersonDTO  {
	
	private BigDecimal iCUIT;
	
	public BigDecimal getCUIT() {
		return iCUIT;
	}
	public void setCUIT(BigDecimal pCUIT) {
		this.iCUIT = pCUIT;
	}
	
}
