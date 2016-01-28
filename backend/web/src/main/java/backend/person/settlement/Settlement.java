package backend.person.settlement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Un Settlement representa un pago realizaco por un cliente.
 */

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.Person;
import backend.person.city.CityConsts;
import backend.person.city.state.State;



@Entity
@Table(name = "settlement", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class Settlement {
	
	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es la que usa postgresql por defecto
	@SequenceGenerator(name = "settlement_id_seq", sequenceName = "settlement_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settlement_id_seq")
	private Long iId;
	
	@Column(name = "date")
	@NotNull(message = SettlementConsts.cDATE_NOTNULL_VIOLATION_MESSAGE)
	private Date iDate;
	
	@Column(name = "amount")
	@DecimalMin(value="0", message= SettlementConsts.cAMOUNT_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = SettlementConsts.cAMOUNT_NOTNULL_VIOLATION_MESSAGE)
	private BigDecimal iAmount;
	
	@Column(name = "concept")
	@Size(max=250, message= SettlementConsts.cCONCEPT_SIZE_VIOLATION_MESSAGE)
	private String iConcept;
	
	@Column(name = "chek_number")
	@Size(max=8, message= SettlementConsts.cCHEK_NUMBER_SIZE_VIOLATION_MESSAGE)
	private String iChekNumber;
	
	@Column(name = "discounted")
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

	public String getChekNumber() {
		return iChekNumber;
	}

	public void setChekNumber(String iChekNumber) {
		this.iChekNumber = iChekNumber;
	}

	public boolean getDiscounted() {
		return iDiscounted;
	}
	
	public boolean isDiscounted() {
		return iDiscounted;
	}

	public void setDiscounted(boolean iDiscounted) {
		this.iDiscounted = iDiscounted;
	}

	/**public Person getPerson() {
		return iPerson;
	}

	public void setPerson(Person pPerson) {
		this.iPerson = pPerson;
	}*/

}
