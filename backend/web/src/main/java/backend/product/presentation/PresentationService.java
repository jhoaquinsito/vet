package backend.product.presentation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.utils.EntityValidator;

/**
 * Un <code>PresentationService</code> representa un conjunto de servicios
 * relacionados a <code>Presentation</code>.
 * 
 * Este conjunto de servicios tiene: el repositorio de presentaciones:
 * <strong>PresentationRepository</strong>.
 * 
 */
public class PresentationService {

	private PresentationRepository iPresentationRepository;
	private EntityValidator iEntityValidator;
	
	// constantes para mensajes de excepciones:
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Presentación no válida: el nombre ya existe en la base de datos.";

	/**
	 * Constructor.
	 */
	public PresentationService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iPresentationRepository = mAppContext.getBean(PresentationRepository.class);
		this.iEntityValidator = new EntityValidator();
	}

	/**
	 * Método que permite leer todas las presentaciones.
	 * 
	 * @return lista de presentaciones
	 */
	public Iterable<Presentation> getAll() {
		return this.iPresentationRepository.findAll();
	}

	/**
	 * Método que guarda una presentación.
	 * 
	 * @param pPresentationToSave
	 *            presentación a guardar
	 * @return presentación tal cual quedó guardada
	 * @throws BusinessException
	 */
	public Presentation save(Presentation pPresentationToSave) throws BusinessException {
		// valido la presentación
		this.iEntityValidator.validate(pPresentationToSave);
		
		// si va a ser una inserción, valido que no exista
		if (pPresentationToSave.getId()==null && this.exists(pPresentationToSave)) {
			// ya existe el nombre
			throw new BusinessException(PresentationService.cEXISTING_NAME_EXCEPTION_MESSAGE);
		}

		// guardo la presentación
		Presentation mPresentationSaved = this.iPresentationRepository.save(pPresentationToSave);

		return mPresentationSaved;
	}

	/**
	 * Método que crea una presentación.
	 * 
	 * @param pNewPresentation
	 *            nueva presentación
	 * @return presentación tal cual quedó almacenada
	 * @throws BusinessException
	 *             errores de negocio encontrados
	 */
	public Presentation create(Presentation pNewPresentation) throws BusinessException {
		// guardo la presentación
		Presentation mPresentationSaved;

		if (!this.exists(pNewPresentation)) {
			// no existe, OK para guardarla:
			mPresentationSaved = this.save(pNewPresentation);
		} else {
			// ya existe la presentación
			throw new BusinessException(PresentationService.cEXISTING_NAME_EXCEPTION_MESSAGE);
		}

		return mPresentationSaved;
	}

	/**
	 * Método que checkea si la presentación existe en la base de datos (por
	 * nombre).
	 * 
	 * @param pPresentation
	 *            presentación a checkear si existe
	 * @return true si existe, false si no existe en la base de datos
	 */
	private Boolean exists(Presentation pPresentation) {
		// obtengo la lista completa
		Iterable<Presentation> mPresentationList = this.getAll();

		Boolean mPresentationExists = false;
		// itero para verificar si existe
		for (Presentation bPresentation : mPresentationList) {
			// me fijo si tienen el mismo nombre
			if (bPresentation.getName().equals(pPresentation.getName())) {
				mPresentationExists = true;
			}
		}

		return mPresentationExists;
	}

}
