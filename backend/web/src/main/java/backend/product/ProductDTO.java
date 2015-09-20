package backend.product;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import backend.product.category.CategoryDTO;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.PresentationDTO;

public class ProductDTO {

	private Long iId;
	private String iName;
	private String iDescription;
	private BigDecimal iMinimumStock;
	private BigDecimal iUnitPrice;
	private Timestamp iLastUpdateOn;
	private Timestamp iDeletedOn;
	private String iLastUpdateUser;
	private BigDecimal iCost;
	private BigDecimal iUtility;
	private CategoryDTO iCategory;
	private ManufacturerDTO iManufacturer;
	private MeasureUnitDTO iMeasureUnit;
	private PresentationDTO iPresentation;
	private Set<DrugDTO> iDrugs;

	public Long getId() {
		return iId;
	}

	public void setId(Long pId) {
		this.iId = pId;
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

	public CategoryDTO getCategory() {
		return iCategory;
	}

	public void setCategory(CategoryDTO pCategory) {
		this.iCategory = pCategory;
	}

	public ManufacturerDTO getManufacturer() {
		return iManufacturer;
	}

	public void setManufacturer(ManufacturerDTO pManufacturer) {
		this.iManufacturer = pManufacturer;
	}

	public MeasureUnitDTO getMeasureUnit() {
		return iMeasureUnit;
	}

	public void setMeasureUnit(MeasureUnitDTO pMeasureUnit) {
		this.iMeasureUnit = pMeasureUnit;
	}

	public PresentationDTO getPresentation() {
		return iPresentation;
	}

	public void setPresentation(PresentationDTO pPresentation) {
		this.iPresentation = pPresentation;
	}

	public Set<DrugDTO> getDrugs() {
		return iDrugs;
	}

	public void setDrugs(Set<DrugDTO> pDrugs) {
		this.iDrugs = pDrugs;
	}

}
