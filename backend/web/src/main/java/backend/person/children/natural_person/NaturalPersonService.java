package backend.person.children.natural_person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import backend.exception.BusinessException;
import backend.exception.ExceptionUtils;
import backend.utils.EntityValidator;

@Service
public class NaturalPersonService {
	
	private static final String cDELETED_NATURAL_PERSON_EXCEPTION_MESSAGE = "La persona que se desea recuperar no esta activa.";
	private static final String cCANNOT_SAVE_NATURAL_PERSON_EXCEPTION_MESSAGE = "No se pudo guardar la persona.";
	private static final String cNATURAL_PERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "Se violó alguna de las restricciones de la base de datos.";
	@Autowired private NaturalPersonRepository iNaturalPersonRepository;
	private EntityValidator iEntityValidator;
	
	/**
	 * Constructor.
	 */
	public NaturalPersonService() {
		super();
		this.iEntityValidator = new EntityValidator();
	}
	
	/**
	 * Método que permite obtener la lista de personas reales de forma
	 * completa.
	 * @return Iterable<NaturalPerson> Lista de personas reales.
	 * @throws BusinessException
	 */
	public Iterable<NaturalPerson> getAll() throws BusinessException {
		return this.iNaturalPersonRepository.findAll();
	}
	
	/**
	 * Método que guarda una persona física en la base de datos.
	 * 
	 * @param pNaturalPersonToSave persona física a guardar
	 * @return persona física guardada o null, si hubo un error y no se pudo guardar
	 * 
	 * @throws BusinessException Excepcion producida si la persona física no se pudo
	 * guardar por problemas de restricciones en las tablas de la base de datos.
	 */
	private NaturalPerson tryToSave(NaturalPerson pNaturalPersonToSave) throws BusinessException {
		NaturalPerson mNaturalPersonSaved = null;
		
		try {
			mNaturalPersonSaved = this.iNaturalPersonRepository.save(pNaturalPersonToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){
			
			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(NaturalPersonService.cNATURAL_PERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(NaturalPersonService.cNATURAL_PERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE,bDataIntegrityViolationException);
		}
		
		return mNaturalPersonSaved;
	}
	
	/**
	 * Método que permite guardar una persona física. Puede ser una persoan nueva (creación) o
	 * una persona existente que esté modificada (actualización).
	 * 
	 * @param pNaturalPersonToSave persona que se desea guardar
	 * @return persona física tal cual quedó guardada
	 * @throws BusinessException 
	 */
	public NaturalPerson save(NaturalPerson pNaturalPersonToSave) throws BusinessException {
		// verifico que la persona que se intenta guardar no esté eliminada
		// o que traiga un identificador que no existe
		if (pNaturalPersonToSave.getId() != null){
			try{
				this.get(pNaturalPersonToSave.getId());
			} catch (BusinessException bBusinessException){
				// o no existe o sino está eliminada
				throw new BusinessException(NaturalPersonService.cCANNOT_SAVE_NATURAL_PERSON_EXCEPTION_MESSAGE, bBusinessException);
			}
		}
		
		//valido la entidad
		this.iEntityValidator.validate(pNaturalPersonToSave);
		
		// guardo el producto
		NaturalPerson mNaturalPersonSaved = this.tryToSave(pNaturalPersonToSave);
		
		
		return mNaturalPersonSaved;
	}

	/**
	 * Método que permite obtener una persona física a partir de su identificador.
	 * @param pId identificador de la persona física
	 * @return persona física encontrada
	 * @throws BusinessException intentó obtener una persona física eliminada lógicamente
	 */
	public NaturalPerson get(Long pId)throws BusinessException{
		NaturalPerson mStoredNaturalPerson = this.iNaturalPersonRepository.findOne(pId);
		
		if ((mStoredNaturalPerson != null) && (!mStoredNaturalPerson.isActive())){
			throw new BusinessException(NaturalPersonService.cDELETED_NATURAL_PERSON_EXCEPTION_MESSAGE);
		}
		
		return mStoredNaturalPerson;
	}


}
