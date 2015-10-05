package backend.product.category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

/**
 * Un <code>CategoryService</code> representa un conjunto de servicios relacionados a <code>Category</code>.
 * 
 * Este conjunto de servicios tiene:
 * TODO repositorio de categorías debería decir:
 * el repositorio de productos: <strong>ProductRepository</strong>.
 * 
 * @author genesis
 *
 */

public class CategoryService {
	
	private CategoryRepository iCategoryRepository;
	
	/**
	 * Constructor.
	 */
	public CategoryService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iCategoryRepository = (CategoryRepository) mAppContext.getBean(CategoryRepository.class);
	}
	
	/**
	 * Método que permite guardar categorias. Puede ser una categoria nueva (creación) o
	 * una categoria existente que está modificado (actualización)
	 * @param pCategoryToSave producto que se desea guardar
	 * @return Categoria tal cual quedó guardada
	 * @throws BusinessException 
	 */
	public Category save(Category pCategoryToSave) throws BusinessException {
		// Valido si el producto tiene datos válidos
		this.validate(pCategoryToSave);
		
		// Guardo la categoria
		Category mCategorySaved = this.iCategoryRepository.save(pCategoryToSave);
		
		return mCategorySaved;
	}
	
	public List<Category> getByName(String pCategoryName) throws BusinessException {
		List<Category> mResult = new ArrayList<Category>();
		
		try {
			Iterator<Category> mIterator = this.iCategoryRepository.findAll().iterator();
			
			while(mIterator.hasNext()){
				Category mCategory = mIterator.next();
				if(mCategory.getName().toLowerCase().equalsIgnoreCase(pCategoryName.toLowerCase()))
					mResult.add(mCategory);
			}
		// TODO catchear la excepción que corresponde y no una general
		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}
		
		
		return mResult;
		
	}
	
	/**
	 * Metodo que permite validar una <strong>Categoria</strong>, antes de enviarlo a la capa de Repository
	 * Estas validaciones corresponden directamente con el modelo.
	 * 
	 * @param Category : Categoria a Validar
	 * @return void
	 * @throws BusinessException - Una excepcion de negocio con el detalle del error.
	 */
	private void validate(Category pCategory) throws BusinessException{

		//TODO revisar las convenciones de código
		//TODO usar constantes y no hardcodear:
		String friendlyMessage = "Category NO valido: ";
		
		//(String pClassName, String pMethodName, String pExMessage, String pRequestUrl, HttpStatus pStatusCode) {
		
		if(pCategory.getName().length() == 0){
			//TODO usar constantes y no hardcodear:
			throw new BusinessException(friendlyMessage + "  Nombre vacio ");
		}
		if(pCategory.getName().length() > 30){
			//TODO usar constantes y no hardcodear:
			throw new BusinessException(friendlyMessage + " Nombre excede el limite de caracteres (30) ");
		}
		if(!this.getByName(pCategory.getName()).isEmpty()){
			//TODO usar constantes y no hardcodear:
			throw new BusinessException(friendlyMessage + " Ya existe una categoría bajo el mismo nombre( " + pCategory.getName() + " )");
		}
		
		
	}

	public Iterable<Category> getAll() {
		return this.iCategoryRepository.findAll();
	}
	/*
	public Category getById(long id){
		return this.iCategoryRepository.findOne(id);
	}
	*/
	
}
