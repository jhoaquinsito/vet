package backend.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Una <code>Category</code> es una representación de una categoría de productos.
 * Una categoría de productos tiene:
 * un <strong>Id</strong>, 
 * y un <strong>Name</strong>.
 */
@Entity
@Table(name = "category", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class Category {

	@Id
	@Column(name="id", nullable = false)
	// los id se generan a partir de la siguiente secuencia:
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque es 
	// la que usa postgresql por defecto
    @SequenceGenerator(name="category_id_seq", sequenceName="category_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="category_id_seq")
	private Long iId;
	
	@Column(name="name", unique = false, nullable = false, length = 100)
	private String iName;

	public Category() {
		super();
	}

	public Category(String pName) {
		super();
		this.iName = pName;
	}

	public Long getId() {
		return iId;
	}

	public String getName() {
		return iName;
	}

	public void setName(String pName) {
		this.iName = pName;
	}

}
