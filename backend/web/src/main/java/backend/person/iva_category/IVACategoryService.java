package backend.person.iva_category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.utils.EntityValidator;

/**
 * Un <code>IVACategoryService</code> representa un conjunto de servicios
 * relacionados a <code>IVACategory</code>. *
 */

public class IVACategoryService {

	private IVACategoryRepository iIVACategoryRepository;
	private EntityValidator iEntityValidator;

	// constantes para mensajes de excepciones:
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Categoría de IVA no válida: el nombre ya existe en la base de datos.";

	/**
	 * Constructor.
	 */
	public IVACategoryService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iIVACategoryRepository = mAppContext.getBean(IVACategoryRepository.class);
		this.iEntityValidator = new EntityValidator();
	}

	/**
	 * Método que permite guardar categorías de IVA. Puede ser una categoría nueva
	 * (creación) o una categoría existente que está modificado (actualización)
	 * 
	 * @param pIVACategoryToSave
	 *            categoría que se desea guardar
	 * @return Categoría tal cual quedó guardada
	 * @throws BusinessException
	 */
	public IVACategory save(IVACategory pIVACategoryToSave) throws BusinessException {
		// Valido si el producto tiene datos válidos
		this.iEntityValidator.validate(pIVACategoryToSave);

		// Guardo la categoría
		IVACategory mIVACategorySaved = this.iIVACategoryRepository.save(pIVACategoryToSave);

		return mIVACategorySaved;
	}

	// TODO comentar este método
	public List<IVACategory> getByDescription(String pIVACategoryDes) throws BusinessException {
		List<IVACategory> mResult = new ArrayList<IVACategory>();

		try {
			Iterator<IVACategory> mIterator = this.iIVACategoryRepository.findAll().iterator();

			while (mIterator.hasNext()) {
				IVACategory mIVACategory = mIterator.next();
				if (mIVACategory.getDescription().toLowerCase().equalsIgnoreCase(pIVACategoryDes.toLowerCase()))
					mResult.add(mIVACategory);
			}
			// TODO catchear la excepción que corresponde y no una general
		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}

		return mResult;

	}

	// TODO para que sirve esto? refactorizarlo y documentarlo
	public Iterable<IVACategory> getAll() throws BusinessException {
		
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iDescription");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			return this.iIVACategoryRepository.findAll(sort);
	

		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}
		

	}

}

