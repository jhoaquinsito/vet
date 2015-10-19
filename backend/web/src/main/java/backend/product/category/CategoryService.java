package backend.product.category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.product.Product;

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

public class CategoryService {

	private CategoryRepository iCategoryRepository;

	// constantes para mensajes de excepciones:
	private static final String cNULL_NAME_EXCEPTION_MESSAGE = "Categoría no válida: nombre sin valor.";
	private static final String cEMPTY_NAME_EXCEPTION_MESSAGE = "Categoría no válida: nombre vacío.";
	private static final String cLONG_NAME_EXCEPTION_MESSAGE = "Categoría no válida: nombre con más de 30 caracteres.";
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Categoría no válida: el nombre ya existe en la base de datos.";

	/**
	 * Constructor.
	 */
	public CategoryService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iCategoryRepository = mAppContext.getBean(CategoryRepository.class);
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
		// Valido si el producto tiene datos válidos
		this.validate(pCategoryToSave);

		// Guardo la categoria
		Category mCategorySaved = this.iCategoryRepository.save(pCategoryToSave);

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

	/**
	 * Metodo que permite validar una <strong>Categoria</strong>, antes de
	 * enviarlo a la capa de Repository Estas validaciones corresponden
	 * directamente con el modelo.
	 * 
	 * @param Category
	 *            : Categoria a Validar
	 * @return void
	 * @throws BusinessException
	 *             - Una excepcion de negocio con el detalle del error.
	 */
	private void validate(Category pCategory) throws BusinessException {

		String mCategoryName = pCategory.getName();

		if (pCategory.getName().length() == 0) {
			throw new BusinessException(CategoryService.cEMPTY_NAME_EXCEPTION_MESSAGE);
		}
		if (pCategory.getName().length() > 30) {
			throw new BusinessException(CategoryService.cLONG_NAME_EXCEPTION_MESSAGE);
		}
		// verifica si existia una categoria con el mismo nombre, distinta de la
		// que estoy tratando de insertar (distinto id)
		if (!this.getByName(mCategoryName).isEmpty()) {
			for (Category bCategory : this.getByName(mCategoryName)) {
				if (bCategory.getId() != pCategory.getId()) {
					throw new BusinessException(CategoryService.cEXISTING_NAME_EXCEPTION_MESSAGE);
				}
			}
		}

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
