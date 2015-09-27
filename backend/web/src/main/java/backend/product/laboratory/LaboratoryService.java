package backend.product.laboratory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

/**
 * Un <code>LaboratoryService</code> representa un conjunto de servicios relacionados a <code>Laboratory</code>.
 * 
 * Este conjunto de servicios tiene:
 * el repositorio de productos: <strong>LaboratoryRepository</strong>.
 * 
 * @author genesis
 *
 */

public class LaboratoryService {
	
	private LaboratoryRepository iLaboratoryRepository;
	
	/**
	 * Constructor.
	 */
	public LaboratoryService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iLaboratoryRepository = (LaboratoryRepository) mAppContext.getBean(LaboratoryRepository.class);
	}
	
	/**
	 * Método que permite guardar laboratorios. Puede ser un laboratorio nuevo (creación) o
	 * un laboratorio existente que está modificado (actualización)
	 * @param pLaboratoryToSave producto que se desea guardar
	 * @return Laboratory tal cual quedó guardada
	 * @throws BusinessException 
	 */
	public Laboratory save(Laboratory pLaboratoryToSave) throws BusinessException {
		// Valido si el producto tiene datos válidos
		this.validate(pLaboratoryToSave);
		
		// Guardo el laboratorio
		Laboratory mLaboratorySaved = this.iLaboratoryRepository.save(pLaboratoryToSave);
		
		return mLaboratorySaved;
	}
	
	public List<Laboratory> getByName(String pLaboratoryName) throws BusinessException {
		List<Laboratory> mResult = new ArrayList<Laboratory>();
		
		try {
			Iterator<Laboratory> mIterator = this.iLaboratoryRepository.findAll().iterator();
			
			while(mIterator.hasNext()){
				Laboratory mLaboratory = mIterator.next();
				if(mLaboratory.getName().toLowerCase().equalsIgnoreCase(pLaboratoryName.toLowerCase()))
					mResult.add(mLaboratory);
			}
		} catch (Exception e) {
			throw new BusinessException("LaboratoryService","getByName", e.getMessage(),  "Se ha producido una excepcion", HttpStatus.CONFLICT);
		}
		
		
		return mResult;
		
	}
	
	/**
	 * Metodo que permite validar una <strong>Laboratorio</strong>, antes de enviarlo a la capa de Repository
	 * Estas validaciones corresponden directamente con el modelo.
	 * 
	 * @param Laboratory : Categoria a Validar
	 * @return void
	 * @throws BusinessException - Una excepcion de negocio con el detalle del error.
	 */
	private void validate(Laboratory pLaboratory) throws BusinessException{

		String friendlyMessage = "Laboratory NO valido: ";
		
		//(String pClassName, String pMethodName, String pExMessage, String pRequestUrl, HttpStatus pStatusCode) {
		
		if(pLaboratory.getName().length() == 0){
			throw new BusinessException("LaboratoryService","Laboratory no válido", "validate",  friendlyMessage + " Nombre vacio ", HttpStatus.CONFLICT);
		}
		if(pLaboratory.getName().length() > 30){
			throw new BusinessException("LaboratoryService","Laboratory no válido", "validate",  friendlyMessage + " Nombre excede el limite de caracteres (30) ", HttpStatus.CONFLICT);
		}
		if(!this.getByName(pLaboratory.getName()).isEmpty()){
			throw new BusinessException("LaboratoryService","Laboratory no válido", "validate",  friendlyMessage + " Ya existe un laboratorio bajo el mismo nombre ( " + pLaboratory.getName() + " )", HttpStatus.CONFLICT);
		}
		
		
	}

	public Iterable<Laboratory> getAll() {
		return this.iLaboratoryRepository.findAll();
	}
	
	public Laboratory getById(long id){
		return this.iLaboratoryRepository.findOne(id);
	}
	
	
}
