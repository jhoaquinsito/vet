package backend.core;

import java.util.ArrayList;
import java.util.List;

import backend.exception.BusinessException;
import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductService;
import backend.product.category.Category;
import backend.product.category.CategoryDTO;
import backend.product.category.CategoryService;
import backend.product.drug.Drug;
import backend.product.drug.DrugDTO;
import backend.product.drug.DrugService;
import backend.product.manufacturer.Manufacturer;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.manufacturer.ManufacturerService;
import backend.product.measure_unit.MeasureUnit;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.measure_unit.MeasureUnitService;
import backend.product.presentation.Presentation;
import backend.product.presentation.PresentationDTO;
import backend.product.presentation.PresentationService;
import backend.utils.OrikaMapperFactory;
import ma.glasnost.orika.MapperFacade;

/**
 * <code>CommandAndQueries</code> representa el conjunto de comandos y consultas posibles de realizar al dominio.
 * Los comandos y consultas son utilizados a nivel de presentación (RestController) y esta clase se encarga
 * de convertir los DTO recibidos en objetos de dominio, implementar el comando o la consulta deseada utilizando
 * objetos del dominio y luego convertir la respuesta a DTO para responder a la capa de presentación (si
 * es necesario).
 * 
 * El conjunto de comandos y consultas tiene asociado:
 * un mappeador de objetos (DTO vs dominio, o viceversa): <strong>Mapper</strong>.
 * 
 * @author tomas
 *
 */
public class CommandAndQueries {
	// MapperFacade de Orika (librería para mapping)
	private MapperFacade iMapper; 
	private static final String cPRODUCT_NULL_EXCEPTION_MESSAGE = "El producto no tiene valores.";
	
	
	/**
	 * Constructor.
	 */
	public CommandAndQueries() {
		super();
		// obtengo un mapper facade de la factory Orika
		this.iMapper = OrikaMapperFactory.getMapperFacade();
	}
	
	/**
	 * Este método es un comando que permite guardar un producto.
	 * 
	 * @param pProductDTO producto a guardar
	 * @return identificador del producto guardado
	 * @throws BusinessException 
	 */
	// TODO cambiar nombre de este método por "createProduct"
	public Long saveProduct(ProductDTO pProductDTO) throws BusinessException {
		
		ProductService mProductService = new ProductService();
		
		// map dto to domain object
		Product mProduct;
		if (pProductDTO != null){
			mProduct = iMapper.map(pProductDTO, Product.class);
		} else {
			throw new BusinessException(CommandAndQueries.cPRODUCT_NULL_EXCEPTION_MESSAGE);
		}
		
		
		mProduct = mProductService.save(mProduct);
		
		return mProduct.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Productos
	 * @return lista de Producto
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<ProductDTO> getProducts() throws BusinessException{
		ProductService mProductService = new ProductService();
		
		Iterable<Product> mProduct = mProductService.getAll();
		
		List<ProductDTO> mProductDTOList = new ArrayList<ProductDTO>();
		
		for (Product bProduct : mProduct){
			mProductDTOList.add(this.iMapper.map(bProduct,ProductDTO.class));
		}
		
		return mProductDTOList;
	}
	
	// TODO comentar este método
	public List<PresentationDTO> getPresentations() throws BusinessException{
		PresentationService mPresentationService = new PresentationService();
		
		Iterable<Presentation> mPresentations = mPresentationService.getAll();
		
		List<PresentationDTO> mPresentationDTOList = new ArrayList<PresentationDTO>();
		
		for (Presentation bPresentation : mPresentations){
			mPresentationDTOList.add(this.iMapper.map(bPresentation,PresentationDTO.class));
		}
		
		return mPresentationDTOList;
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de unidades de medida.
	 * @return lista de unidades de medida
	 * @throws BusinessException 
	 */
	public List<MeasureUnitDTO> getMeasureUnits() throws BusinessException{
		MeasureUnitService mMeasureUnitService = new MeasureUnitService();
		
		Iterable<MeasureUnit> mMeasureUnits = mMeasureUnitService.getAll();
		
		List<MeasureUnitDTO> mMeasureUnitDTOList = new ArrayList<MeasureUnitDTO>();
		
		for (MeasureUnit bMeasureUnit : mMeasureUnits){
			mMeasureUnitDTOList.add(this.iMapper.map(bMeasureUnit,MeasureUnitDTO.class));
		}
		
		return mMeasureUnitDTOList;
	}	
	

	/**
	 * Este método es una consulta que devuelve la lista completa de Categorías
	 * @return lista de Categorias
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<CategoryDTO> getCategorys() throws BusinessException{
		CategoryService mCategoryService = new CategoryService();
		
		Iterable<Category> mCategory = mCategoryService.getAll();
		
		List<CategoryDTO> mCategoryDTOList = new ArrayList<CategoryDTO>();
		
		for (Category bCategory : mCategory){
			mCategoryDTOList.add(this.iMapper.map(bCategory,CategoryDTO.class));
		}
		
		return mCategoryDTOList;
	}

	/**
	 * Este método es un comando que permite guardar una Categoría
	 * @param pCategory : Categoría a guardar
	 * @return identificador de la categoria guardado
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public long saveCategory(CategoryDTO pCategory) throws BusinessException {
		CategoryService mCategoryService = new CategoryService();
		
		Category bCategory = new Category();
		
		bCategory = this.iMapper.map(pCategory,Category.class);
		
		return this.iMapper.map(mCategoryService.save(bCategory),CategoryDTO.class).getId();
		
	}

	/**
	 * Este método es una consulta que devuelve la lista completa de laboratorios
	 * @return lista de laboratorios
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public List<ManufacturerDTO> getManufacturers() throws BusinessException{
		ManufacturerService mManufacturerService = new ManufacturerService();
		
		Iterable<Manufacturer> mManufacturer = mManufacturerService.getAll();
		
		List<ManufacturerDTO> mManufacturerDTOList = new ArrayList<ManufacturerDTO>();
		
		for (Manufacturer bManufacturer : mManufacturer){
			mManufacturerDTOList.add(this.iMapper.map(bManufacturer,ManufacturerDTO.class));
		}
		
		return mManufacturerDTOList;
	}

	/**
	 * Este método es un comando que permite guardar un Laboratorio
	 * @param pManufacturer : Laboratorio a guardar
	 * @return identificador del laboratorio guardado
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public long saveManufacturer(ManufacturerDTO pManufacturer) throws BusinessException {
		ManufacturerService mManufacturerService = new ManufacturerService();
		
		Manufacturer bManufacturer = new Manufacturer();
		
		bManufacturer = this.iMapper.map(pManufacturer,Manufacturer.class);
		
		return this.iMapper.map(mManufacturerService.save(bManufacturer),ManufacturerDTO.class).getId();
		
	}



	/**
	 * Este método es un comando que permite guardar una presentación.
	 * @param pPresentationDTO presentación a guardar
	 * @return identificador de la presentación guardada
	 * @throws BusinessException errores de negocio producidos
	 */
	public Long createPresentation(PresentationDTO pPresentationDTO) throws BusinessException{
		PresentationService mPresentationService = new PresentationService();
		
		// map dto to domain object
		Presentation mPresentation = iMapper.map(pPresentationDTO, Presentation.class);
		
		mPresentation = mPresentationService.create(mPresentation);
		
		return mPresentation.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de drogas
	 * @return lista de drogas
	 */
	public List<DrugDTO> getDrugs(){
		DrugService mDrugService = new DrugService();
		
		Iterable<Drug> mDrugs = mDrugService.getAll();
		
		List<DrugDTO> mDrugDTOList = new ArrayList<DrugDTO>();
		
		for (Drug bDrug : mDrugs){
			mDrugDTOList.add(this.iMapper.map(bDrug,DrugDTO.class));
		}
		
		return mDrugDTOList;
	}
	
	/**
	 * Este método es un comando que permite guardar una droga.
	 * @param pDrugDTO presentación a guardar
	 * @return identificador de la presentación guardada
	 * @throws BusinessException errores de negocio producidos
	 */
	public Long createDrug(DrugDTO pDrugDTO) throws BusinessException{
		DrugService mDrugService = new DrugService();
		
		// map dto to domain object
		Drug mDrug = iMapper.map(pDrugDTO, Drug.class);
		
		mDrug = mDrugService.create(mDrug);
		
		return mDrug.getId();
	}
	
	/**
	 * Este método es una consulta que obtiene un producto a partir de
	 * su identificador.
	 * @param pProductId identificador del producto
	 * @return producto encontrado
	 * @throws BusinessException el producto estaba eliminado lógicamente
	 */
	public ProductDTO getProduct(Long pProductId) throws BusinessException{
		ProductService mProductService = new ProductService();
		
		Product mProduct = mProductService.get(pProductId);
		
		ProductDTO mProductDTO = this.iMapper.map(mProduct, ProductDTO.class);
		
		return mProductDTO;
	}
	
	/**
	 * Este método es un comando que elimina un producto a partir de su identificador.
	 * Al eliminar el producto, sus los lotes asociados son eliminados físicamente.
	 * @param pProductId identificador del producto a eliminar
	 * @throws BusinessException errores de negocio al intentar hacer la operación
	 */
	public void deleteProduct(Long pProductId) throws BusinessException{
		ProductService mProductService = new ProductService();
		
		// elimino el producto
		mProductService.delete(pProductId);
		
	}

}
