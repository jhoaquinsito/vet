package backend.product.measure_unit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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

	@Column(name = "name", unique = false, nullable = false, length = 100)
	private String iName;

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

	public Long getId() {
		return iId;
	}

}
