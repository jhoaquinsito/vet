package backend.product;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import backend.product.category.Category;
import backend.product.drug.Drug;
import backend.product.manufacturer.Manufacturer;
import backend.product.measure_unit.MeasureUnit;
import backend.product.presentation.Presentation;

/**
* Un <code>Product</code> es una representación de un tipo de producto. Un tipo
* de producto tiene: un <strong>Id</strong>, un <strong>Name</strong>, una
* <strong>Description</strong>, una cantidad minima de stock:
* <strong>MinimumStock</strong>, un precio unitario: <strong>UnitPrice</strong>
* , una fecha de cuando fue la ultima vez que se actualizó:
* <strong>LastUpdateOn</strong>, una fecha de cuando se eliminó:
* <strong>DeletedOn</strong>, un usuario que realizó la ultima modificación en
* el producto: <strong>LastUpdateUser</strong>, un costo unitario:
* <strong>Cost</strong>, una utilidad o ganancia: <strong>Utility</strong>, una
* <strong>Category</strong>, un laboratorio que lo fabricó:
* <strong>Manufacturer</strong>, una unidad de medida:
* <strong>MeasureUnit</strong>, una presentación (envase, inyectable, etc.):
* <strong>Presentation</strong>, una serie de drogas que lo constituyen:
* <strong>Drugs</strong>.
 */
@Entity
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class Product {

	public Product() {
		super();
	}

	@Id
	@Column(name="id", nullable = false)
	// los id se generan a partir de la siguiente secuencia
	// NOTA: la nomenclatura del nombre de la secuencia debe respetarse porque es 
	// la que usa postgresql por defecto
    @SequenceGenerator(name="product_id_seq", sequenceName="product_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_id_seq")
	private Long iId;
	
	@Column(name="name", unique = false, nullable = false, length = 100)
	private String iName;
	
	@Column(name="description")
	private String iDescription;
	
	@Column(name="minimum_stock")
	private BigDecimal iMinimumStock;
	
	@Column(name="unit_price")
	private BigDecimal iUnitPrice;

	// TODO refactorizar estas columnas de auditoría en otra entidad
	@Column(name="last_update_on")
	private Timestamp iLastUpdateOn;
	
	@Column(name="deleted_on")
	private Timestamp iDeletedOn;
	
	// TODO este atributo no debería guardar un user id, debería tener la referencia al usuario vía Hibernate
	// TODO este user id guarda la ultima vez que se modificó el producto? o tambien guarda la ultima vez que
	// se modificó por ejemplo el lote, o la droga del producto? si es así hay que cambiarle el nombre
	@Column(name="last_update_user")
	private String iLastUpdateUser;

	@Column(name="cost")
	private BigDecimal iCost;
	
	@Column(name="utility")
	private BigDecimal iUtility;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="category")
	private Category iCategory;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="manufacturer")
	private Manufacturer iManufacturer;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="measure_unit")
	private MeasureUnit iMeasureUnit;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="presentation")
	private Presentation iPresentation;

	// TODO revisar si corresponden PERSIST and MERGE
	// TODO revisar porque debo usar EAGER acá
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
	@JoinTable(
        name="product_drugs",
        joinColumns=@JoinColumn(name="product"),
        inverseJoinColumns=@JoinColumn(name="drug")
    )
    private Set<Drug> iDrugs;

	
	

	@Override
	public String toString() {
		return "Product [iId=" + iId + ", iName=" + iName + ", iDescription=" + iDescription + ", iMinimumStock="
				+ iMinimumStock + ", iUnitPrice=" + iUnitPrice + ", iLastUpdateOn=" + iLastUpdateOn + ", iDeletedOn="
				+ iDeletedOn + ", iLastUpdateUser=" + iLastUpdateUser + ", iCost=" + iCost + ", iUtility=" + iUtility
				+ ", iCategory=" + iCategory + ", iManufacturer=" + iManufacturer + ", iMeasureUnit=" + iMeasureUnit
				+ ", iPresentation=" + iPresentation + ", iDrugs=" + iDrugs + "]";
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

	public BigDecimal getUnitPrice() {
		return iUnitPrice;
	}

	public void setUnitPrice(BigDecimal pUnitPrice) {
		this.iUnitPrice = pUnitPrice;
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

	public Long getId() {
		return iId;
	}

	public BigDecimal getCost() {
		return iCost;
	}

	public void setCost(BigDecimal pCost) {
		this.iCost = pCost;
	}

	public BigDecimal getUtility() {
		return iUtility;
	}

	public void setUtility(BigDecimal pUtility) {
		this.iUtility = pUtility;
	}

	public Category getCategory() {
		return iCategory;
	}

	public void setCategory(Category pCategory) {
		this.iCategory = pCategory;
	}

	public Manufacturer getManufacturer() {
		return iManufacturer;
	}

	public void setManufacturer(Manufacturer pManufacturer) {
		this.iManufacturer = pManufacturer;
	}

	public MeasureUnit getMeasureUnit() {
		return iMeasureUnit;
	}

	public void setMeasureUnit(MeasureUnit pMeasureUnit) {
		this.iMeasureUnit = pMeasureUnit;
	}

	public Presentation getPresentation() {
		return iPresentation;
	}

	public void setPresentation(Presentation pPresentation) {
		this.iPresentation = pPresentation;
	}

	public Set<Drug> getDrugs() {
		return iDrugs;
	}

	public void setDrugs(Set<Drug> pDrugs) {
		this.iDrugs = pDrugs;
	}

}