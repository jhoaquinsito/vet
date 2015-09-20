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

	public Long iId;
	public String iName;
	public String iDescription;
	public BigDecimal iMinimumStock;
	public BigDecimal iUnitPrice;
	public Timestamp iLastUpdateOn;
	public Timestamp iDeletedOn;
	public String iLastUpdateUser;
	public BigDecimal iCost;
	public BigDecimal iUtility;
	public CategoryDTO iCategory;
	public ManufacturerDTO iManufacturer;
	public MeasureUnitDTO iMeasureUnit;
	public PresentationDTO iPresentation;
	public Set<DrugDTO> iDrugs;

}
