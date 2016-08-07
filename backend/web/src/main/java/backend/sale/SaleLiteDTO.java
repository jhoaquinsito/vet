package backend.sale;


import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.settlement.SettlementDTO;
import backend.saleline.SaleLineLiteDTO;

/**
 * 
 * @author gonzalo
 *
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SaleLiteDTO {

	private Long iId;

	private Date iDate;
	  
	private boolean iInvoiced ;

	private boolean iPaied_out ;

	private Long iPerson;

	//Este campo es calculado al final.
	private String iPersonName;
	
	private Set<SaleLineLiteDTO> iSaleLines;
	
	private SettlementDTO iSettlement;
	
	private String iPayForm;	
	
	
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

	public Long getPerson() {
		return iPerson;
	}

	public void setPerson(Long pPerson) {
		this.iPerson = pPerson;
	}

	public String getPersonName() {
		return iPersonName;
	}

	public void setPersonName(String pPersonName) {
		this.iPersonName = pPersonName;
	}

	public Set<SaleLineLiteDTO> getSaleLines() {
		return iSaleLines;
	}

	public void setSaleLines(Set<SaleLineLiteDTO> pSaleLines) {
		this.iSaleLines = pSaleLines;
	}

	public SettlementDTO getSettlement() {
		return iSettlement;
	}

	public void setSettlement(SettlementDTO pSettlement) {
		this.iSettlement = pSettlement;
	}
	
	public String getPayForm() {
		return iPayForm;
	}

	public void setPayForm(String pPayForm) {
		this.iPayForm = pPayForm;
	}
}
