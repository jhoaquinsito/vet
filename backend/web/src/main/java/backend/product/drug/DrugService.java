package backend.product.drug;

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
import backend.product.category.CategoryService;
import backend.utils.EntityValidator;

/**
 * Un <code>DrugService</code> representa un conjunto de servicios
 * relacionados a <code>Drug</code>.
 * 
 * Este conjunto de servicios tiene: el repositorio de drogas:
 * <strong>DrugRepository</strong>.
 * 
 */
@Service
public class DrugService {

	@Autowired private DrugRepository iDrugRepository;
	private EntityValidator iEntityValidator;
	// constantes para mensajes de excepciones:
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Droga no válida: el nombre ya existe en la base de datos.";
	private static final String cDRUG_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "Hubo un problema con alguna de las restricciones de la base de datos. Muy probablemente el nombre de una Droga, o alguno de sus hijas, violó una restricción unique.";
	
	/**
	 * Constructor.
	 */
	public DrugService() {
		super();
		this.iEntityValidator = new EntityValidator();
	}

	/**
	 * Método que permite leer todas las drogas.
	 * 
	 * @return lista de drogas , ordenadas alfabeticamente de forma ascendente.
	 */
	public Iterable<Drug> getAll() {
		
		//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
		Direction direction = Direction.ASC;
		List<String> properties = new ArrayList<String>();
		properties.add("iName");
		//Creamos el objeto Sort para pasarle al query.
		Sort sort = new Sort(direction,properties);
		
		//Usamos el nuevo findAll con el Sorting.
		return this.iDrugRepository.findAll(sort);
		
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
		
		Drug mDrugSaved = null;
		
		try {
			// valido la droga
			this.iEntityValidator.validate(pDrugToSave);
			
			// si va a ser una inserción, valido que no exista
			if (pDrugToSave.getId()==null && this.exists(pDrugToSave)) {
				// ya existe el nombre
				throw new BusinessException(DrugService.cEXISTING_NAME_EXCEPTION_MESSAGE);
			}
	
			// guardo la droga
			mDrugSaved = this.iDrugRepository.save(pDrugToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){

			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(DrugService.cDRUG_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE  + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(DrugService.cDRUG_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE ,bDataIntegrityViolationException);
			
		}
		
		
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
