package backend.product.measure_unit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.product.measure_unit.MeasureUnit;
import backend.product.measure_unit.MeasureUnitRepository;
import backend.product.measure_unit.MeasureUnitService;

public class MeasureUnitService {
	
	private MeasureUnitRepository iMeasureUnitRepository;
	// constantes para mensajes de excepciones:
	private static final String cNULL_NAME_EXCEPTION_MESSAGE = "Unidad de medida no válida: nombre sin valor.";
	private static final String cEMPTY_NAME_EXCEPTION_MESSAGE = "Unidad de medida no válida: nombre vacío.";
	private static final String cLONG_NAME_EXCEPTION_MESSAGE = "Unidad de medida no válida: nombre con más de 30 caracteres.";
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Unidad de medida no válida: el nombre ya existe en la base de datos.";

	/**
	 * Constructor.
	 */
	public MeasureUnitService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iMeasureUnitRepository = (MeasureUnitRepository) mAppContext.getBean(MeasureUnitRepository.class);
	}

	/**
	 * Método que permite leer todas las unidades de medida.
	 * 
	 * @return lista de unidades de medida
	 */
	public Iterable<MeasureUnit> getAll() {
		return this.iMeasureUnitRepository.findAll();
	}

	/**
	 * Método que guarda una unidad de medida.
	 * 
	 * @param pMeasureUnitToSave
	 *            unidad de medida a guardar
	 * @return unidad de medida tal cual quedó guardada
	 * @throws BusinessException
	 */
	public MeasureUnit save(MeasureUnit pMeasureUnitToSave) throws BusinessException {
		// valido la unidad de medida
		this.validate(pMeasureUnitToSave);

		// guardo la unidad de medida
		MeasureUnit mMeasureUnitSaved = this.iMeasureUnitRepository.save(pMeasureUnitToSave);

		return mMeasureUnitSaved;
	}

	/**
	 * Método que crea una unidad de medida.
	 * 
	 * @param pNewMeasureUnit
	 *            nueva unidad de medida
	 * @return unidad de medida tal cual quedó almacenada
	 * @throws BusinessException
	 *             errores de negocio encontrados
	 */
	public MeasureUnit create(MeasureUnit pNewMeasureUnit) throws BusinessException {
		// guardo la unidad de medida
		MeasureUnit mMeasureUnitSaved;

		if (!this.exists(pNewMeasureUnit)) {
			// no existe, OK para guardarla:
			mMeasureUnitSaved = this.save(pNewMeasureUnit);
		} else {
			// ya existe la unidad de medida
			throw new BusinessException(MeasureUnitService.cEXISTING_NAME_EXCEPTION_MESSAGE);
		}

		return mMeasureUnitSaved;
	}

	/**
	 * Método que valida si una presentación es correcta en términos del
	 * dominio.
	 * 
	 * @param pMeasureUnit
	 *            presentación a validar
	 * @throws BusinessException
	 */
	private void validate(MeasureUnit pMeasureUnit) throws BusinessException {
		if (pMeasureUnit.getName() != null) {
			if (pMeasureUnit.getName().length() > 0) {
				if (pMeasureUnit.getName().length() <= 30) {
					// nombre OK
				} else {
					// nombre con más caracteres que lo permitido
					throw new BusinessException(MeasureUnitService.cLONG_NAME_EXCEPTION_MESSAGE);
				}
			} else {
				// nombre vacío
				throw new BusinessException(MeasureUnitService.cEMPTY_NAME_EXCEPTION_MESSAGE);
			}
		} else {
			// nombre con valor NULL
			throw new BusinessException(MeasureUnitService.cNULL_NAME_EXCEPTION_MESSAGE);
		}
	}

	/**
	 * Método que checkea si la presentación existe en la base de datos (por
	 * nombre).
	 * 
	 * @param pMeasureUnit
	 *            presentación a checkear si existe
	 * @return true si existe, false si no existe en la base de datos
	 */
	private Boolean exists(MeasureUnit pMeasureUnit) {
		// obtengo la lista completa
		Iterable<MeasureUnit> mMeasureUnitList = this.getAll();

		Boolean mMeasureUnitExists = false;
		// itero para verificar si existe
		for (MeasureUnit bMeasureUnit : mMeasureUnitList) {
			// me fijo si tienen el mismo nombre
			if (bMeasureUnit.getName().equals(pMeasureUnit.getName())) {
				mMeasureUnitExists = true;
			}
		}

		return mMeasureUnitExists;
	}

}
