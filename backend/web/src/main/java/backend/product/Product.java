package backend.product;


import java.math.BigDecimal;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import backend.product.batch.Batch;
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
* , un costo unitario:
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
	
	@OneToMany(mappedBy="iProduct", fetch=FetchType.EAGER)
	private Set<Batch> iBatches;

	// TODO revisar si corresponden PERSIST and MERGE
	// TODO revisar porque debo usar EAGER acá
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
	@JoinTable(
        name="product_drugs",
        joinColumns=@JoinColumn(name="product"),
        inverseJoinColumns=@JoinColumn(name="drug")
    )
    private Set<Drug> iDrugs;


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

	public Set<Batch> getBatches() {
		return iBatches;
	}

	public void setBatches(Set<Batch> pBatches) {
		this.iBatches = pBatches;
	}

}