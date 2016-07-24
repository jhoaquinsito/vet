package backend.person.children.legal_person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import backend.exception.BusinessException;
import backend.exception.ExceptionUtils;
import backend.person.PersonDTO;
import backend.person.children.natural_person.NaturalPerson;
import backend.person.children.natural_person.NaturalPersonService;
import backend.utils.EntityValidator;

@Service
public class LegalPersonService {
	
	private EntityValidator iEntityValidator;
	private static final String cLEGALPERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "La persona legal no puede guardarse, se está violando alguna de sus reestriciones.";
	private static final String cLEGALPERSON_CLIENT_EXISTENT_CUIT   = "La persona legal no puede guardarse, actualmente existe un cliente   activo con el mismo CUIT.";
	private static final String cLEGALPERSON_SUPPLIER_EXISTENT_CUIT = "La persona legal no puede guardarse, actualmente existe un proveedor activo con el mismo CUIT.";
	private static final String cLEGALPERSON_NOT_ACTIVE_EXCEPTION_MESSAGE   = "La persona legal que desea consultar no está activa.";
	
	@Autowired private LegalPersonRepository iLegalPersonRepository;
	
	/**
	 * Constructor.
	 */
	public LegalPersonService() {
		super();
		this.iEntityValidator = new EntityValidator();
	}
	
	/**
	 * Método que permite obtener la lista de personas legales de forma
	 * completa.
	 * @return Iterable<LegalPerson> Lista de personas legales.
	 * @throws BusinessException
	 */
	public Iterable<LegalPerson> getAll() throws BusinessException {
		Iterable<LegalPerson> iLegalPersons = this.iLegalPersonRepository.findAll();
		ArrayList<LegalPerson> iLegalPersonsActive = new ArrayList<LegalPerson>();
		for(LegalPerson iLegalPerson : iLegalPersons)
		{
			if(iLegalPerson.isActive())
				iLegalPersonsActive.add(iLegalPerson);
		}
		return iLegalPersonsActive;
	}

	/**
	 * Método que permite eliminar una persona legal a partir de su identificador.
	 * @param pId identificador de la persona legal a eliminar
	 * @throws BusinessException errores de negocio al intentar realizar la operación
	 */
	public void delete(Long pId) throws BusinessException {
		// busco el persona legal por id
		LegalPerson mLegalPersonToDelete = this.iLegalPersonRepository.findOne(pId);
	
		mLegalPersonToDelete.setActive(false);
		
		// almaceno el persona legal desactivado y sin los lotes
		this.iLegalPersonRepository.save(mLegalPersonToDelete);
		
	}

	/**
	 * Método que permite guardar Personas Legales. Puede ser una persona nueva(creación) o
	 * una existente que esté modificada (actualización).
	 * 
	 * @param pLegalPersonToSave persona legal que se desea guardar
	 * @return persona legal tal cual quedó guardado
	 * @throws BusinessException 
	 */
	public LegalPerson save(LegalPerson pLegalPersonToSave) throws BusinessException {
		// verifico que la persona legal que se intenta guardar no esté eliminada
		// o que traiga un identificador que no existe
		// TODO refactor esto:
		LegalPerson mStoredLegalPerson;
		if (pLegalPersonToSave.getId() != null){
			 mStoredLegalPerson = this.iLegalPersonRepository.findOne(pLegalPersonToSave.getId());
		}
		
		//valido la entidad
		this.iEntityValidator.validate(pLegalPersonToSave);
		
		//valido casos específicos.
		this.Validate(pLegalPersonToSave);
		
		// guardo el producto
		LegalPerson mLegalPersonSaved = this.tryToSave(pLegalPersonToSave);
		
		
		return mLegalPersonSaved;
	}

	/**
	 * Método que permite obtener una persona legal a partir de su identificador.
	 * @param pId identificador de la persona legal
	 * @return persona legal encontrada
	 * @throws BusinessException 
	 */
	public LegalPerson get(Long pId) throws BusinessException{
		LegalPerson mStoredLegalPerson = this.iLegalPersonRepository.findOne(pId);
		
		if((mStoredLegalPerson != null) && (!mStoredLegalPerson.isActive()))
			throw new BusinessException(LegalPersonService.cLEGALPERSON_NOT_ACTIVE_EXCEPTION_MESSAGE);
		return mStoredLegalPerson;
	}
	
	/**
	 * Método que guarda un producto en la base de datos.
	 * 
	 * @param pProductToSave producto a guardar
	 * @return producto guardado o null, si hubo un error y no se pudo guardar
	 * 
	 * @throws BusinessException Excepcion producida si el producto no se pudo
	 * guardar por problemas de restricciones en las tablas de la base de datos.
	 */
	private LegalPerson tryToSave(LegalPerson pLegalPersonToSave) throws BusinessException {
		LegalPerson mLegalPersonSaved = null;
		
		try {
			mLegalPersonSaved = this.iLegalPersonRepository.save(pLegalPersonToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){
			
			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(LegalPersonService.cLEGALPERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(LegalPersonService.cLEGALPERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE,bDataIntegrityViolationException);
			
		} catch (Exception ex){
			throw new BusinessException(ex.getMessage(),ex);
		}
		
		return mLegalPersonSaved;
	}
	
	/***
	 * Este método permite hacer una validación específica, sobre aquellos casos que exceden las capacidades
	 * automáticas de validación por medio de JPA.
	 * En este método se haran sucesivas llamadas a métodos privados para validar distintos casos.
	 * 
	 * @param pLegalPersonToSave : Persona legal que se está intentando guardar.
	 * @throws BusinessException : Excepcion producida si el la persona no se podrá guardar porque no valida alguno de los casos.
	 */
	public void Validate(LegalPerson pLegalPersonToSave) throws BusinessException {
		
		//1° Validación específica - Validar CUIT.
		this.ValidateCuit(pLegalPersonToSave);

	}
	
	/**
	 * Este método privado se encarga de validar un caso específico.
	 * CASO: 
	 * - Validar que si la persona es CLIENTE o PROVEEDOR, el CUIT no debe repetirse con alguno
	 * de los CLIENTES / PROVEEDORES (reespectivamente) <strong> ACTIVOS </strong>.
	 * @param pLegalPersonToSave
	 * @throws BusinessException
	 */
	private void ValidateCuit(LegalPerson pLegalPersonToSave)throws BusinessException {
		Iterable<LegalPerson> mLegalPersons = this.getAll();
				
		for (LegalPerson bLegalPerson : mLegalPersons){
			//Caso de validacion 1. La persona legal a guardar es CLIENTE, el CUIT no debe repetirse con algun cliente existente activo.
			if (   bLegalPerson.getId()    !=  pLegalPersonToSave.getId()    &&
				   bLegalPerson.isClient() &&  pLegalPersonToSave.isClient() && bLegalPerson.isActive()  
				&& bLegalPerson.getCUIT().compareTo(pLegalPersonToSave.getCUIT()) == 0)
				throw new BusinessException(LegalPersonService.cLEGALPERSON_CLIENT_EXISTENT_CUIT);
				
			
			if (   bLegalPerson.getId()      !=  pLegalPersonToSave.getId()      &&
				   bLegalPerson.isSupplier() &&  pLegalPersonToSave.isSupplier() && bLegalPerson.isActive()  
			    && bLegalPerson.getCUIT().compareTo(pLegalPersonToSave.getCUIT()) == 0)
					throw new BusinessException(LegalPersonService.cLEGALPERSON_SUPPLIER_EXISTENT_CUIT);		
		}
	}
}
