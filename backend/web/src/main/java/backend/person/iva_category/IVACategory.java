package backend.person.iva_category;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.*;

import backend.person.PersonConsts;

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
	
	@Column(name = "percentage")
	@Digits(integer=2, fraction=2, message= IVACategoryConsts.cPERCENTAGE_DIGITS_VIOLATION_MESSAGE)
	@NotNull(message = IVACategoryConsts.cPERCENTAGE_NOTNULL_VIOLATION_MESSAGE)
	private BigDecimal iPercentage;

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
	
	public BigDecimal getPercentage() {
		return iPercentage;
	}

	public void setPercentage(BigDecimal iPercentage) {
		this.iPercentage = iPercentage;
	}

}
