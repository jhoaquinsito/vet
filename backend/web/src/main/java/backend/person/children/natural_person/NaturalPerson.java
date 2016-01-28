package backend.person.children.natural_person;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import backend.person.Person;

// TODO documentar

@Entity
@PrimaryKeyJoinColumn(name="person_id")
@Table(name = "natural_person", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class NaturalPerson extends Person {


	@Column(name = "national_id", unique=true)
	@NotNull(message = NaturalPersonConsts.cNATIONAL_ID_NOTNULL_VIOLATION_MESSAGE)
	private Integer iNationalId;

	@Column(name = "last_name")
	@Size(min=1, max=100, message= NaturalPersonConsts.cLAST_NAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = NaturalPersonConsts.cLAST_NAME_NOTNULL_VIOLATION_MESSAGE)
	private String iLastName;

	public Integer getNationalId() {
		return iNationalId;
	}

	public void setNationalId(Integer iNationalId) {
		this.iNationalId = iNationalId;
	}

	public String getLastName() {
		return iLastName;
	}

	public void setLastName(String iLastName) {
		this.iLastName = iLastName;
	}

}