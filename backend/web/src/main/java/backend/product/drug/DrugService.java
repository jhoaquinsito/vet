package backend.product.drug;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

/**
 * Un <code>DrugService</code> representa un conjunto de servicios
 * relacionados a <code>Drug</code>.
 * 
 * Este conjunto de servicios tiene: el repositorio de drogas:
 * <strong>DrugRepository</strong>.
 * 
 */
public class DrugService {

	private DrugRepository iDrugRepository;
	// constantes para mensajes de excepciones:
	private static final String cNULL_NAME_EXCEPTION_MESSAGE = "Droga no válida: nombre sin valor.";
	private static final String cEMPTY_NAME_EXCEPTION_MESSAGE = "Droga no válida: nombre vacío.";
	private static final String cLONG_NAME_EXCEPTION_MESSAGE = "Droga no válida: nombre con más de 30 caracteres.";
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Droga no válida: el nombre ya existe en la base de datos.";

	/**
	 * Constructor.
	 */
	public DrugService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iDrugRepository = (DrugRepository) mAppContext.getBean(DrugRepository.class);
	}

	/**
	 * Método que permite leer todas las drogas.
	 * 
	 * @return lista de drogas
	 */
	public Iterable<Drug> getAll() {
		return this.iDrugRepository.findAll();
	}

	/**
	 * Método que guarda una droga.
	 * 
	 * @param pDrugToSave
	 *            droga a guardar
	 * @return droga tal cual quedó guardada
	 * @throws BusinessException
	 */
	public Drug save(Drug pDrugToSave) throws BusinessException {
		// valido la droga
		this.validate(pDrugToSave);
		
		// si va a ser una inserción, valido que no exista
		if (pDrugToSave.getId()==null && this.exists(pDrugToSave)) {
			// ya existe el nombre
			throw new BusinessException(DrugService.cEXISTING_NAME_EXCEPTION_MESSAGE);
		}

		// guardo la droga
		Drug mDrugSaved = this.iDrugRepository.save(pDrugToSave);

		return mDrugSaved;
	}

	/**
	 * Método que crea una droga.
	 * 
	 * @param pNewDrug
	 *            nueva droga
	 * @return droga tal cual quedó almacenada
	 * @throws BusinessException
	 *             errores de negocio encontrados
	 */
	public Drug create(Drug pNewDrug) throws BusinessException {

		Drug mDrugSaved;

		if (!this.exists(pNewDrug)) {
			// no existe, OK para guardarla:
			mDrugSaved = this.save(pNewDrug);
		} else {
			// ya existe la droga
			throw new BusinessException(DrugService.cEXISTING_NAME_EXCEPTION_MESSAGE);
		}

		return mDrugSaved;
	}

	/**
	 * Método que valida si una droga es correcta en términos del
	 * dominio.
	 * 
	 * @param pDrug
	 *            droga a validar
	 * @throws BusinessException
	 */
	private void validate(Drug pDrug) throws BusinessException {
		if (pDrug.getName() != null) {
			if (pDrug.getName().length() > 0) {
				if (pDrug.getName().length() <= 30) {
					// nombre OK
				} else {
					// nombre con más caracteres que lo permitido
					throw new BusinessException(DrugService.cLONG_NAME_EXCEPTION_MESSAGE);
				}
			} else {
				// nombre vacío
				throw new BusinessException(DrugService.cEMPTY_NAME_EXCEPTION_MESSAGE);
			}
		} else {
			// nombre con valor NULL
			throw new BusinessException(DrugService.cNULL_NAME_EXCEPTION_MESSAGE);
		}
	}

	/**
	 * Método que checkea si la droga existe en la base de datos (por
	 * nombre).
	 * 
	 * @param pDrug
	 *            droga a checkear si existe
	 * @return true si existe, false si no existe en la base de datos
	 */
	private Boolean exists(Drug pDrug) {
		// obtengo la lista completa
		Iterable<Drug> mDrugList = this.getAll();

		Boolean mDrugExists = false;
		// itero para verificar si existe
		for (Drug bDrug : mDrugList) {
			// me fijo si tienen el mismo nombre
			if (bDrug.getName().equalsIgnoreCase(pDrug.getName())) {
				mDrugExists = true;
			}
		}

		return mDrugExists;
	}
}
