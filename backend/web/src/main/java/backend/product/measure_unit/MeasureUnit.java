package backend.product.measure_unit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Una <code>MeasureUnit</code> es una representación una unidad de medida. Una
 * unidad de medida tiene: un <strong>Id</strong>, y un <strong>Name</strong>.
 */
@Entity
@Table(name = "measure_unit", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class MeasureUnit {

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es
	// la que usa postgresql por defecto
	@SequenceGenerator(name = "measure_unit_id_seq", sequenceName = "measure_unit_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measure_unit_id_seq")
	private Long iId;

	@Column(name = "name", unique = true, nullable = false, length = 100)
	@Size(min=1, max=30, message= MeasureUnitConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = MeasureUnitConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iName;
	
	@Column(name = "abbreviation", unique = true, nullable = false, length = 5)
	@Size(min=1, max=5, message= MeasureUnitConsts.cABBREVIATION_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = MeasureUnitConsts.cABBREVIATION_NOTNULL_VIOLATION_MESSAGE)
	private String iAbbreviation;

	public MeasureUnit(String pName) {
		super();
		this.iName = pName;
	}

	public MeasureUnit() {
		super();
	}

	public String getName() {
		return iName;
	}

	public void setName(String pName) {
		this.iName = pName;
	}

	public String getAbbreviation() {
		return iAbbreviation;
	}

	public void setAbbreviation(String pAbbreviation) {
		this.iAbbreviation = pAbbreviation;
	}
	
	public Long getId() {
		return iId;
	}
	
	public void setId(Long pId) {
		this.iId = pId;
	}

}
