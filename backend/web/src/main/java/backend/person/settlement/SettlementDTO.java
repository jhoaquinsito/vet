package backend.person.settlement;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SettlementDTO {

	private Long iId;
	private Date iDate;
	private BigDecimal iAmount;
	private String iConcept;
	private String iCheckNumber;
	private boolean iDiscounted;

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

	public String getCheckNumber() {
		return iCheckNumber;
	}

	public void setCheckNumber(String iCheckNumber) {
		this.iCheckNumber = iCheckNumber;
	}

	public boolean isDiscounted() {
		return iDiscounted;
	}

	public void setDiscounted(boolean iDiscounted) {
		this.iDiscounted = iDiscounted;
	}

}
