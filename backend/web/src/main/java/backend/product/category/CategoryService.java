package backend.product.category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.exception.ExceptionUtils;
import backend.person.iva_category.IVACategoryService;
import backend.utils.EntityValidator;

/**
 * Un <code>CategoryService</code> representa un conjunto de servicios
 * relacionados a <code>Category</code>.
 * 
 * Este conjunto de servicios tiene: el repositorio de categorias:
 * <strong>ProductRepository</strong>.
 * 
 * @author genesis
 *
 */

@Service
public class CategoryService {
	@Autowired private CategoryRepository iCategoryRepository;
	private EntityValidator iEntityValidator;

	// constantes para mensajes de excepciones:
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Categoría no válida: el nombre ya existe en la base de datos.";
	private static final String cCATEGORY_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "Hubo un problema con alguna de las restricciones de la base de datos. Muy probablemente el nombre de una Categoría, o alguno de sus hijas, violó una restricción unique.";
	
	/**
	 * Constructor.
	 */
	public CategoryService() {
		super();
		this.iEntityValidator = new EntityValidator();
	}

	/**
	 * Método que permite guardar categorias. Puede ser una categoria nueva
	 * (creación) o una categoria existente que está modificado (actualización)
	 * 
	 * @param pCategoryToSave
	 *            producto que se desea guardar
	 * @return Categoria tal cual quedó guardada
	 * @throws BusinessException
	 */
	public Category save(Category pCategoryToSave) throws BusinessException {
		Category mCategorySaved = null;
		try {
			// Valido si el producto tiene datos válidos
			this.iEntityValidator.validate(pCategoryToSave);
	
			// Guardo la categoria
			mCategorySaved = this.iCategoryRepository.save(pCategoryToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){

			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(CategoryService.cCATEGORY_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE  + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(CategoryService.cCATEGORY_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE ,bDataIntegrityViolationException);
			
		}
		return mCategorySaved;
	}

	// TODO comentar este método
	public List<Category> getByName(String pCategoryName) throws BusinessException {
		List<Category> mResult = new ArrayList<Category>();

		try {
			Iterator<Category> mIterator = this.iCategoryRepository.findAll().iterator();

			while (mIterator.hasNext()) {
				Category mCategory = mIterator.next();
				if (mCategory.getName().toLowerCase().equalsIgnoreCase(pCategoryName.toLowerCase()))
					mResult.add(mCategory);
			}
			// TODO catchear la excepción que corresponde y no una general
		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}

		return mResult;

	}

	// TODO para que sirve esto? refactorizarlo y documentarlo
	public Iterable<Category> getAll() throws BusinessException {
		
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iName");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			return this.iCategoryRepository.findAll(sort);
	

		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}
		

	}

}
