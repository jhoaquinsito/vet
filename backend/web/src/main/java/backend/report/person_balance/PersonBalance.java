package backend.report.person_balance;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import backend.person.Person;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Table(name = "person_balance")
public class PersonBalance implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id @OneToOne
  	@JoinColumn(name = "person_id")
	private Person iPerson;

	@Column(name = "debt_total")
	private BigDecimal iDebtTotal;

	@Column(name = "credit_total")
	private BigDecimal iCreditTotal;

	public Person getPerson() {
		return iPerson;
	}

	public void setPerson(Person pPerson) {
		this.iPerson = pPerson;
	}

	public BigDecimal getDebtTotal() {
		return iDebtTotal;
	}

	public void setDebtTotal(BigDecimal pDebtTotal) {
		this.iDebtTotal = pDebtTotal;
	}

	public BigDecimal getCreditTotal() {
		return iCreditTotal;
	}

	public void setCreditTotal(BigDecimal pCreditTotal) {
		this.iCreditTotal = pCreditTotal;
	}

}
