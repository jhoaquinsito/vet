package backend.product.presentation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

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
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Presentación no válida: el nombre ya existe en la base de datos.";

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
	 * @throws BusinessException 
	 */
	public Iterable<Presentation> getAll() throws BusinessException {
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iName");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			return this.iPresentationRepository.findAll(sort);
	

		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}

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
					// nombre con más caracteres que lo permitido
					throw new BusinessException(PresentationService.cLONG_NAME_EXCEPTION_MESSAGE);
				}
			} else {
				// nombre vacío
				throw new BusinessException(PresentationService.cEMPTY_NAME_EXCEPTION_MESSAGE);
			}
		} else {
			// nombre con valor NULL
			throw new BusinessException(PresentationService.cNULL_NAME_EXCEPTION_MESSAGE);
		}
	}

	/**
	 * Método que checkea si la presentación existe en la base de datos (por
	 * nombre).
	 * 
	 * @param pPresentation
	 *            presentación a checkear si existe
	 * @return true si existe, false si no existe en la base de datos
	 * @throws BusinessException 
	 */
	private Boolean exists(Presentation pPresentation) throws BusinessException {
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
