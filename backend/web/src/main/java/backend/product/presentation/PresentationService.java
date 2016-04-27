package backend.product.presentation;

import java.util.ArrayList;
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
import backend.product.measure_unit.MeasureUnitService;
import backend.utils.EntityValidator;

/**
 * Un <code>PresentationService</code> representa un conjunto de servicios
 * relacionados a <code>Presentation</code>.
 * 
 * Este conjunto de servicios tiene: el repositorio de presentaciones:
 * <strong>PresentationRepository</strong>.
 * 
 */
@Service
public class PresentationService {

	@Autowired private PresentationRepository iPresentationRepository;
	private EntityValidator iEntityValidator;
	
	// constantes para mensajes de excepciones:
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Presentación no válida: el nombre ya existe en la base de datos.";
	private static final String cPRESENTATION_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "Hubo un problema con alguna de las restricciones de la base de datos. Muy probablemente el nombre de una Presentación, o alguno de sus hijas, violó una restricción unique.";
	
	/**
	 * Constructor.
	 */
	public PresentationService() {
		super();
		this.iEntityValidator = new EntityValidator();
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
		Presentation mPresentationSaved = null;
		try {
			// valido la presentación
			this.iEntityValidator.validate(pPresentationToSave);
			
			// si va a ser una inserción, valido que no exista
			if (pPresentationToSave.getId()==null && this.exists(pPresentationToSave)) {
				// ya existe el nombre
				throw new BusinessException(PresentationService.cEXISTING_NAME_EXCEPTION_MESSAGE);
			}
	
			// guardo la presentación
			mPresentationSaved = this.iPresentationRepository.save(pPresentationToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){
			
			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(PresentationService.cPRESENTATION_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE  + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(PresentationService.cPRESENTATION_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE ,bDataIntegrityViolationException);
			
		}
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
