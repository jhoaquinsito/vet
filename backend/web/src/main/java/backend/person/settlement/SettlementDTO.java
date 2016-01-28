package backend.person.settlement;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.PersonDTO;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SettlementDTO {

	private Long iId;
	private Date iDate;
	private BigDecimal iAmount;
	private String iConcept;
	private String iChekNumber;
	private boolean iDiscounted;
	//private PersonDTO iPerson;

	public Long getId() {
		return iId;
	}

	public void setId(Long iId) {
		this.iId = iId;
	}

	public Date getDate() {
		return iDate;
	}

	public void setDate(Date iDate) {
		this.iDate = iDate;
	}

	public BigDecimal getAmount() {
		return iAmount;
	}

	public void setAmount(BigDecimal iAmount) {
		this.iAmount = iAmount;
	}

	public String getConcept() {
		return iConcept;
	}

	public void setConcept(String iConcept) {
		this.iConcept = iConcept;
	}

	public String getChekNumber() {
		return iChekNumber;
	}

	public void setChekNumber(String iChekNumber) {
		this.iChekNumber = iChekNumber;
	}

	public boolean isDiscounted() {
		return iDiscounted;
	}

	public void setDiscounted(boolean iDiscounted) {
		this.iDiscounted = iDiscounted;
	}

}
