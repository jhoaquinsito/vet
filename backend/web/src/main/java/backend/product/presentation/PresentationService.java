package backend.product.presentation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

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
	// constantes para mensajes de excepciones:
	private static final String cNULL_NAME_EXCEPTION_MESSAGE = "Presentación no válida: nombre sin valor.";
	private static final String cEMPTY_NAME_EXCEPTION_MESSAGE = "Presentación no válida: nombre vacío.";
	private static final String cLONG_NAME_EXCEPTION_MESSAGE = "Presentación no válida: nombre con más de 30 caracteres.";

	/**
	 * Constructor.
	 */
	public PresentationService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iPresentationRepository = (PresentationRepository) mAppContext.getBean(PresentationRepository.class);
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
		this.validate(pPresentationToSave);

		// guardo la presentación
		Presentation mPresentationSaved = this.iPresentationRepository.save(pPresentationToSave);

		return mPresentationSaved;
	}

	/**
	 * Método que valida si una presentación es correcta en términos del
	 * dominio.
	 * 
	 * @param pPresentation
	 *            presentación a validar
	 * @throws BusinessException
	 */
	private void validate(Presentation pPresentation) throws BusinessException {
		if (pPresentation.getName() != null) {
			if (pPresentation.getName().length() > 0) {
				if (pPresentation.getName().length() <= 30) {
					// nombre OK
				} else {
					// TODO refactorizar como estamos usando la excepción, con
					// el mensaje debería ser suficiente para lanzarla (los
					// demas son datos que se pueden sacar del contexto)
					
					// nombre con más caracteres que lo permitido
					throw new BusinessException(this.getClass().toString(), "ProductService", "validate",
							PresentationService.cLONG_NAME_EXCEPTION_MESSAGE, HttpStatus.CONFLICT);
				}
			} else {
				// nombre vacío
				throw new BusinessException(this.getClass().toString(), "ProductService", "validate",
						PresentationService.cEMPTY_NAME_EXCEPTION_MESSAGE, HttpStatus.CONFLICT);
			}
		} else {
			// nombre con valor NULL
			throw new BusinessException(this.getClass().toString(), "ProductService", "validate",
					PresentationService.cNULL_NAME_EXCEPTION_MESSAGE, HttpStatus.CONFLICT);
		}
	}

}
