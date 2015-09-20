package backend.utils;

import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.category.Category;
import backend.product.category.CategoryDTO;
import backend.product.drug.Drug;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.Manufacturer;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnit;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.Presentation;
import backend.product.presentation.PresentationDTO;

import java.util.HashSet;

// TODO usar alguna libreria para hacer este trabajo
/**
 * Mappeador provisional hasta que se implemente una librería.
 * Mappea manualmente objetos DTO a objetos de dominio.
 * 
 * @author tomas
 *
 */
public class CustomMapper {
	
	/**
	 * Método que mapea un ProductDTO en un Product.
	 * @param pProductDTO DTO a mapear
	 * @return Product mapeado
	 */
	public Product productDTOToProduct(ProductDTO pProductDTO){
		Product mProduct = new Product();
		
		mProduct.setName(pProductDTO.iName);
		mProduct.setCost(pProductDTO.iCost);
		mProduct.setDeletedOn(pProductDTO.iDeletedOn);
		mProduct.setDescription(pProductDTO.iDescription);
		mProduct.setLastUpdateOn(pProductDTO.iLastUpdateOn);
		mProduct.setLastUpdateUser(pProductDTO.iLastUpdateUser);
		mProduct.setMinimumStock(pProductDTO.iMinimumStock);
		mProduct.setUnitPrice(pProductDTO.iUnitPrice);
		mProduct.setUtility(pProductDTO.iUtility);
		mProduct.setCategory(this.categoryDTOToCategory(pProductDTO.iCategory));
		mProduct.setPresentation(this.presentationDTOToPresentation(pProductDTO.iPresentation));
		mProduct.setMeasureUnit(this.measureUnitDTOToMeasureUnit(pProductDTO.iMeasureUnit));
		mProduct.setManufacturer(this.manufacturerDTOToManufacturer(pProductDTO.iManufacturer));
		// drugs
		HashSet<Drug> mDrugs = new HashSet<Drug>();
		for (DrugDTO bDrugDTO : pProductDTO.iDrugs){
			mDrugs.add(this.drugDTOToDrug(bDrugDTO));
		}
		mProduct.setDrugs(mDrugs);
		
		
		return mProduct;		
	}
	
	/**
	 * Método que mapea un ManufacturerDTO en un Manufacturer.
	 * @param pManufacturerDTO DTO a mapear
	 * @return Manufacturer mapeado
	 */
	public Manufacturer manufacturerDTOToManufacturer(ManufacturerDTO pManufacturerDTO){
		Manufacturer mManufacturer = new Manufacturer();
		
		mManufacturer.setName(pManufacturerDTO.iName);
		
		return mManufacturer;
	}

	/**
	 * Método que mapea un DrugDTO en un Drug.
	 * @param pDrugDTO DTO a mapear
	 * @return Drug mapeado
	 */
	public Drug drugDTOToDrug(DrugDTO pDrugDTO){
		Drug mDrug = new Drug();
		
		mDrug.setName(pDrugDTO.iName);
		
		return mDrug;	
	}

	/**
	 * Método que mapea un PresentationDTO en un Presentation.
	 * @param pPresentationDTO DTO a mapear
	 * @return Presentation mapeado
	 */
	public Presentation presentationDTOToPresentation(PresentationDTO pPresentationDTO){
		Presentation mPresentation = new Presentation();
		
		mPresentation.setName(pPresentationDTO.iName);
		
		return mPresentation;
	}

	/**
	 * Método que mapea un CategoryDTO en un Category.
	 * @param pCategoryDTO DTO a mapear
	 * @return Category mapeado
	 */
	public Category categoryDTOToCategory(CategoryDTO pCategoryDTO){
		Category mCategory = new Category();
		
		mCategory.setName(pCategoryDTO.iName);
		
		return mCategory;
	}

	/**
	 * Método que mapea un MeasureUnitDTO en un MeasureUnit.
	 * @param pMeasureUnitDTO DTO a mapear
	 * @return MeasureUnit mapeado
	 */
	public MeasureUnit measureUnitDTOToMeasureUnit(MeasureUnitDTO pMeasureUnitDTO){
		MeasureUnit mMeasureUnit = new MeasureUnit();
		
		mMeasureUnit.setName(pMeasureUnitDTO.iName);
		
		return mMeasureUnit;
	}

}
