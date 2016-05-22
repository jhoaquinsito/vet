package backend.product.drug;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.*;

/**
 * Una <code>Drug</code> es una representación de una droga utilizada en
 * productos farmacéuticos (de uso veterinario para nuestro dominio). Una droga
 * tiene: un <strong>Id</strong>, y un <strong>Name</strong>.
 */
@Entity
@Table(name = "drug", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
public class Drug {

	@Id
	@Column(name = "id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque
	// es
	// la que usa postgresql por defecto
	@SequenceGenerator(name = "drug_id_seq", sequenceName = "drug_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drug_id_seq")
	private Long iId;

	@Column(name = "name", unique = true, nullable = false, length = 100)
	@Size(min=1, max=30, message= DrugConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = DrugConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
	private String iName;

	public Drug() {
		super();
	}

	public Drug(String pName) {
		super();
		this.iName = pName;
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
