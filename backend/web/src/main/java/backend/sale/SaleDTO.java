package backend.sale;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.PersonDTO;

import backend.saleline.SaleLineDTO;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SaleDTO {

	private Long iId;

	private Date iDate;
	  
	private boolean iInvoiced ;

	private boolean iPaied_out ;

	private PersonDTO iPerson;

	private Set<SaleLineDTO> iSaleLines;
	
	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
	}

	public Date getDate() {
		return iDate;
	}

	public void setDate(Date pDate) {
		this.iDate = pDate;
	}

	public boolean isInvoiced() {
		return iInvoiced;
	}

	public void setInvoiced(boolean pInvoiced) {
		this.iInvoiced = pInvoiced;
	}



	public boolean isPaied_out() {
		return iPaied_out;
	}

	public void setPaied_out(boolean pPaied_out) {
		this.iPaied_out = pPaied_out;
	}

	public PersonDTO getPerson() {
		return iPerson;
	}

	public void setPerson(PersonDTO pPerson) {
		this.iPerson = pPerson;
	}

	public Set<SaleLineDTO> getSaleLines() {
		return iSaleLines;
	}

	public void setSaleLines(Set<SaleLineDTO> pSaleLines) {
		this.iSaleLines = pSaleLines;
	}
	
	
}
