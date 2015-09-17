package backend.product;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "manufacturer", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class Manufacturer {

	public Manufacturer(String iName) {
		super();
		this.iName = iName;
	}

	@Id
	@Column(name="id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque es 
	// la que usa postgresql por defecto
    @SequenceGenerator(name="manufacturer_id_seq", sequenceName="manufacturer_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="manufacturer_id_seq")
	private Integer iId;
	
	@Column(name="name", unique = false, nullable = false, length = 100)
	private String iName;

}
