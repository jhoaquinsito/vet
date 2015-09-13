package backend.product;


import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Un <code>Product</code> es una representación de un tipo de producto. Un tipo de producto tiene un
 * un <strong>Id</strong>, 
 * un <strong>Name</strong>, 
 * una <strong>Description</strong>, 
 * una cantidad minima de stock: <strong>MinimumStock</strong>, 
 * un precio unitario: <strong>UnitaryPrice</strong>, 
 * una fecha de cuando fue la ultima vez que se actualizó: <strong>LastUpdateOn</strong>, 
 * una fecha de cuando se eliminó: <strong>DeletedOn</strong>, 
 * un usuario que realizó la ultima modificación en el producto () <strong>LastUpdateUser</strong>
 */
@Entity
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class Product {

	@Id
	@Column(name="id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque es 
	// la que usa postgresql por defecto
    @SequenceGenerator(name="product_id_seq", sequenceName="product_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_id_seq")
	private Integer iId;
	
	@Column(name="name", unique = false, nullable = false, length = 100)
	private String iName;
	
	@Column(name="description")
	private String iDescription;
	
	// TODO porqué estaba definido como float en la base de datos? float no es exacto. no debería ser numeric?
	@Column(name="minimum_stock")
	private BigDecimal iMinimumStock;
	
	// TODO estaba definido como float en la base de datos pero debe ser un numeric, porque sino corremos el peligro de
	// que en la base de datos se lo redondee
	@Column(name="unitary_price")
	private BigDecimal iUnitaryPrice;
	
	@Column(name="last_update_on")
	private Timestamp iLastUpdateOn;
	
	@Column(name="deleted_on")
	private Timestamp iDeletedOn;
	
	// TODO este atributo no debería guardar un user id, debería tener la referencia al usuario vía Hibernate
	// TODO este user id guarda la ultima vez que se modificó el producto? o tambien guarda la ultima vez que
	// se modificó por ejemplo el lote, o la droga del producto? si es así hay que cambiarle el nombre
	@Column(name="last_update_user")
	private String iLastUpdateUser;
	
	
	@Override
	public String toString() {
		return "Product [iId=" + iId + ", iName=" + iName + ", iDescription=" + iDescription + ", iMinimumStock="
				+ iMinimumStock + ", iUnitaryPrice=" + iUnitaryPrice + ", iLastUpdateOn=" + iLastUpdateOn
				+ ", iDeletedOn=" + iDeletedOn + ", iLastUpdateUser=" + iLastUpdateUser + "]";
	}

	public String getName() {
		return iName;
	}

	public void setName(String pName) {
		this.iName = pName;
	}

	public String getDescription() {
		return iDescription;
	}

	public void setDescription(String pDescription) {
		this.iDescription = pDescription;
	}

	public BigDecimal getMinimumStock() {
		return iMinimumStock;
	}

	public void setMinimumStock(BigDecimal pMinimumStock) {
		this.iMinimumStock = pMinimumStock;
	}

	public BigDecimal getUnitaryPrice() {
		return iUnitaryPrice;
	}

	public void setUnitaryPrice(BigDecimal pUnitaryPrice) {
		this.iUnitaryPrice = pUnitaryPrice;
	}

	public Timestamp getLastUpdateOn() {
		return iLastUpdateOn;
	}

	public void setLastUpdateOn(Timestamp pLastUpdateOn) {
		this.iLastUpdateOn = pLastUpdateOn;
	}

	public Timestamp getDeletedOn() {
		return iDeletedOn;
	}

	public void setDeletedOn(Timestamp pDeletedOn) {
		this.iDeletedOn = pDeletedOn;
	}

	public String getLastUpdateUser() {
		return iLastUpdateUser;
	}

	public void setLastUpdateUser(String pLastUpdateUser) {
		this.iLastUpdateUser = pLastUpdateUser;
	}

	public Integer getId() {
		return iId;
	}

}