package backend.person.iva_category;

import javax.persistence.*;
import javax.validation.constraints.*;

// TODO documentar
@Entity
@Table(name = "IVA_category", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class IVACategory {

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es la que usa postgresql por defecto
	@SequenceGenerator(name = "IVA_category_id_seq", sequenceName = "IVA_category_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IVA_category_id_seq")
	private Long iId;

	@Column(name = "description")
	@Size(min=1, max=100, message= IVACategoryConsts.cDESCRIPTION_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = IVACategoryConsts.cDESCRIPTION_NOTNULL_VIOLATION_MESSAGE)
	private String iDescription;

	public Long getId() {
		return iId;
	}

	public void setId(Long iId) {
		this.iId = iId;
	}

	public String getDescription() {
		return iDescription;
	}

	public void setDescription(String iDescription) {
		this.iDescription = iDescription;
	}

}
