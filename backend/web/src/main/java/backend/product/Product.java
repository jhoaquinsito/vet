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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import backend.person.children.legal_person.LegalPerson;
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
* <strong>Presentation</strong>, una droga:
* <strong>Drug</strong>, una lista de lotes: <strong>Batches</strong> y también
* si está activo o no: <strong>Active</strong>.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames={})})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Product implements Persistable<Long> {

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
	
	@Column(name="name", unique = true, nullable = false, length = 100)
	@Size(min=1, max=100, message= ProductConsts.cNAME_SIZE_VIOLATION_MESSAGE)
	@NotNull(message = ProductConsts.cNAME_NOTNULL_VIOLATION_MESSAGE)
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
	
	@Digits(integer=2, fraction=1)
	@Column(name="iva")
	private BigDecimal iIva;
	
	@Column(name="active")
	private Boolean iActive;

	@ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="category")
	@Valid
	private Category iCategory;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="manufacturer")
	@Valid
	private Manufacturer iManufacturer;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="measure_unit")
	@NotNull(message=ProductConsts.cMEASURE_UNIT_NOTNULL_VIOLATION_MESSAGE)
	@Valid
	private MeasureUnit iMeasureUnit;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="presentation")
	@Valid
	private Presentation iPresentation;
	
	@OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="product")
	@Valid
	private Set<Batch> iBatches;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="drug")
	@Valid
    private Drug iDrug;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="supplier", joinColumns=@JoinColumn(name="product_id"), inverseJoinColumns=@JoinColumn(name="legal_person_id"))
	@Valid
	private Set<LegalPerson> iSuppliers;

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

	@Override
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
	
	public BigDecimal getIva() {
		return iIva;
	}

	public void setIva(BigDecimal pIva) {
		this.iIva = pIva;
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

	public Drug getDrug() {
		return iDrug;
	}

	public void setDrug(Drug pDrug) {
		this.iDrug = pDrug;
	}

	public Set<Batch> getBatches() {
		return iBatches;
	}
	
	/**
	 * Este método permite recuperar el lote que este asociado a una fecha de vencimiento
	 * determinada. Devuelve NULL si el lote no existe.
	 * @param pISODueDate la fecha de vencimiento asociada al lote en formato ISO.
	 * @return mBatch el lote asociada a la fecha de vencimiento
	 */
	public Batch getBatcheByISODueDate(Integer pISODueDate) {
		Batch mBatch = null;
		
		for (Batch bBatch : iBatches) {
			if (bBatch.hasISODueDate(pISODueDate)){
				mBatch = bBatch;
				break;
			}
		}
		
		return mBatch;
	}

	public void setBatches(Set<Batch> pBatches) {
		this.iBatches = pBatches;
		for (Batch bBatch : pBatches) {
			bBatch.setProduct(this);
		}
	}

	public void setId(Long pId) {
		this.iId = pId;
	}

	public Boolean isActive() {
		return iActive;
	}

	public void setActive(Boolean pActive) {
		this.iActive = pActive;
	}

	public Set<LegalPerson> getSuppliers() {
		return iSuppliers;
	}

	public void setSuppliers(Set<LegalPerson> pSuppliers) {
		this.iSuppliers = pSuppliers;
	}
	
	/**
	 * Método que verifica si un producto tiene el stock minimo.
	 * 
	 * @return <strong>true</strong>, si la cantidad de stock actual es mayor que la cantidad de stock
	 * minimo definida para el producto o si no tiene una cantidad de stock minima definida
	 * <strong>false</strong>, en caso de que la cantidad de stock actual es igual o menor a la cantidad
	 * de stock minima definida para el producto.
	 */
	public boolean hasMinimumStock(){
		if (this.getMinimumStock()== null){
			return true;
		}
		
		return this.getTotalStockQuantity().compareTo(this.getMinimumStock()) > 0;
	}
	
	/**
	 * Método que calcula la cantidad total de stock del producto sumando la cantidad de stock de cada
	 * uno de sus lotes.
	 * 
	 * @return cantidad total de stock; 0 (cero), si no tiene lotes asociados
	 */
	public BigDecimal getTotalStockQuantity(){
		BigDecimal mStockCount = new BigDecimal(0);
		for (Batch bBatch : this.getBatches()) {
			mStockCount = mStockCount.add(bBatch.getStock());
		}
		return mStockCount;
	}

	@Override
	public boolean isNew() {
		boolean mProductoIsNew = (this.getId() == null);
		boolean mCategoryIsNew = (this.getCategory() == null || this.getCategory().getId() == null);
		boolean mPresentationIsNew = (this.getPresentation() == null || this.getPresentation().getId() == null);
		boolean mMeasureUnitIsNew = (this.getMeasureUnit() == null || this.getMeasureUnit().getId() == null);
		boolean mDrugIsNew = (this.getDrug() == null || this.getDrug().getId() == null);
		boolean mManufacturerIsNew = (this.getManufacturer() == null || this.getManufacturer().getId() == null);
		
		return mProductoIsNew && mCategoryIsNew && mPresentationIsNew 
				&& mMeasureUnitIsNew && mDrugIsNew && mManufacturerIsNew;
	}

	@PrePersist
	protected
	 void preInsert() {
	   if ( this.isActive() == null ) { this.setActive(true); }
	}
	
	 @PreUpdate
	protected
	 void onPreUpdate() {
		 if ( this.isActive() == null ) { this.setActive(true); }
	 }
	
}