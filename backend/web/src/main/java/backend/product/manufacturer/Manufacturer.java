package backend.product.manufacturer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Un <code>Manufacturer</code> es una representación de un laboratorio o
 * fábrica que haya creado un producto. Una fabrica tiene: un
 * <strong>Id</strong>, y un <strong>Name</strong>.
 */
@Entity
@Table(name = "manufacturer", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class Manufacturer {

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es
	// la que usa postgresql por defecto
	@SequenceGenerator(name = "manufacturer_id_seq", sequenceName = "manufacturer_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manufacturer_id_seq")
	private Long iId;

	@Column(name = "name", unique = false, nullable = false, length = 100)
	@Size(min=1, max=30, message= ManufacturerConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = ManufacturerConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iName;

	public Manufacturer(String pName) {
		super();
		this.iName = pName;
	}

	public Manufacturer() {
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
	
	public void setId(Long pId) {
		this.iId = pId;
	}

}
