package backend.product.measure_unit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.product.measure_unit.MeasureUnit;
import backend.product.measure_unit.MeasureUnitRepository;
import backend.product.measure_unit.MeasureUnitService;
import backend.utils.EntityValidator;

public class MeasureUnitService {
	
	private MeasureUnitRepository iMeasureUnitRepository;
	private EntityValidator iEntityValidator;
	// constantes para mensajes de excepciones:
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Unidad de medida no válida: el nombre ya existe en la base de datos.";

	/**
	 * Constructor.
	 */
	public MeasureUnitService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iMeasureUnitRepository = mAppContext.getBean(MeasureUnitRepository.class);
		this.iEntityValidator = new EntityValidator();
	}

	/**
	 * Método que permite leer todas las unidades de medida.
	 * 
	 * @return lista de unidades de medida
	 * @throws BusinessException 
	 */
	public Iterable<MeasureUnit> getAll() throws BusinessException {
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iName");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			return this.iMeasureUnitRepository.findAll(sort);
	

		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}

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
		this.iEntityValidator.validate(pMeasureUnitToSave);
		
		// si va a ser una inserción, valido que no exista
		if (pMeasureUnitToSave.getId()==null && this.exists(pMeasureUnitToSave)) {
			// ya existe la unidad de medida
			throw new BusinessException(MeasureUnitService.cEXISTING_NAME_EXCEPTION_MESSAGE);
		}
		
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
	 * Método que checkea si la presentación existe en la base de datos (por
	 * nombre).
	 * 
	 * @param pMeasureUnit
	 *            presentación a checkear si existe
	 * @return true si existe, false si no existe en la base de datos
	 * @throws BusinessException 
	 */
	private Boolean exists(MeasureUnit pMeasureUnit) throws BusinessException {
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
