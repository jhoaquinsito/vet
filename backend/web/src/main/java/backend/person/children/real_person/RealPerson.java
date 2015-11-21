package backend.person.children.real_person;

import javax.persistence.*;
import javax.validation.constraints.*;

import backend.person.Person;

// TODO documentar

@Entity
@PrimaryKeyJoinColumn(name="person_id")
@Table(name = "real_person", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class RealPerson extends Person {


	@Column(name = "document_id", unique=true)
	@NotNull(message = RealPersonConsts.cDOCUMENT_ID_NOTNULL_VIOLATION_MESSAGE)
	private Integer iDocumentId;

	@Column(name = "last_name")
	@Size(min=1, max=100, message= RealPersonConsts.cLAST_NAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = RealPersonConsts.cLAST_NAME_NOTNULL_VIOLATION_MESSAGE)
	private String iLastName;

	public Integer getDocumentId() {
		return iDocumentId;
	}

	public void setDocumentId(Integer iDocumentId) {
		this.iDocumentId = iDocumentId;
	}

	public String getLastName() {
		return iLastName;
	}

	public void setLastName(String iLastName) {
		this.iLastName = iLastName;
	}

}