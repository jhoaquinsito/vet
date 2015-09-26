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
		Product mProduct = iMapper.map(pProductDTO, Product.class);
		
		mProduct = mProductService.save(mProduct);
		
		return mProduct.getId();
	}
	
	// TODO comentar este método
	public List<PresentationDTO> getPresentations(){
		PresentationService mPresentationService = new PresentationService();
		
		Iterable<Presentation> mPresentations = mPresentationService.getAll();
		
		List<PresentationDTO> mPresentationDTOList = new ArrayList<PresentationDTO>();
		
		for (Presentation bPresentation : mPresentations){
			mPresentationDTOList.add(this.iMapper.map(bPresentation,PresentationDTO.class));
		}
		
		return mPresentationDTOList;
	}
	
	public List<CategoryDTO> getCategorys(){
		CategoryService mCategoryService = new CategoryService();
		
		Iterable<Category> mCategory = mCategoryService.getAll();
		
		List<CategoryDTO> mCategoryDTOList = new ArrayList<CategoryDTO>();
		
		for (Category bCategory : mCategory){
			mCategoryDTOList.add(this.iMapper.map(bCategory,CategoryDTO.class));
		}
		
		return mCategoryDTOList;
	}

	public long saveCategory(CategoryDTO pCategory) throws BusinessException {
		CategoryService mCategoryService = new CategoryService();
		
		Category bCategory = new Category();
		
		bCategory = this.iMapper.map(pCategory,Category.class);
		
		return this.iMapper.map(mCategoryService.save(bCategory),CategoryDTO.class).getId();
		
	}

	public CategoryDTO getCategory(long pId) throws BusinessException{
		CategoryService mCategoryService = new CategoryService();
		
		return this.iMapper.map(mCategoryService.getById(pId),CategoryDTO.class);
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

}
