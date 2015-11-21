package backend.person.children.legal_person;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.*;

import backend.person.Person;

// TODO documentar

@Entity
@PrimaryKeyJoinColumn(name="person_id")
@Table(name = "legal_person", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class LegalPerson extends Person {

	@Column(name="cuit", unique=true)
	@NotNull(message = LegalPersonConsts.cCUIT_NOTNULL_VIOLATION_MESSAGE)
	@Digits(integer=11, fraction=0, message= LegalPersonConsts.cCUIT_DIGITS_VIOLATION_MESSAGE)
	private BigDecimal iCUIT;

	public BigDecimal getCUIT() {
		return iCUIT;
	}

	public void setCUIT(BigDecimal pCUIT) {
		this.iCUIT = pCUIT;
	}
	
}
