package backend.person.children.legal_person;

import java.util.ArrayList;
import java.util.HashSet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.product.Product;
import backend.product.batch.Batch;

public class LegalPersonService {
	
	private static final String cLEGALPERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "La persona legal no puede guardarse, se está violando alguna de sus reestriciones.";
	private static final String cLEGALPERSON_NOT_ACTIVE_EXCEPTION_MESSAGE   = "La persona legal que desea consultar no está activa.";
	
	private LegalPersonRepository iLegalPersonRepository;
	
	/**
	 * Constructor.
	 */
	public LegalPersonService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iLegalPersonRepository = mAppContext.getBean(LegalPersonRepository.class);
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
		// limpio la lista de lotes asociados
		//TODO - Puede ser que al eliminarse se necesite también eliminar la lista de los productos asociados.
		mLegalPersonToDelete.getProducts().clear();
		
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
		// verifico que el producto que se intenta guardar no esté eliminado
		// o que traiga un identificador que no existe
		LegalPerson mStoredLegalPerson;
		if (pLegalPersonToSave.getId() != null){
			 mStoredLegalPerson = this.iLegalPersonRepository.findOne(pLegalPersonToSave.getId());
		}
		
		//TODO esto debería estar en otra capa anterior (al mapear DTO con domain)
		// asocio el producto a sus lotes (si tiene)
		if (pLegalPersonToSave.getProducts() != null){
			for (Product bProduct : pLegalPersonToSave.getProducts()){
				bProduct.setActive(true);
				if (bProduct.getBatches() != null){
					for (Batch bBatch : bProduct.getBatches()){
						bBatch.setProduct(bProduct);
					}
				} else {
					bProduct.setBatches(new HashSet<Batch>());
				}
			}
		} else {
			pLegalPersonToSave.setProducts(new HashSet<Product>());
		}
		
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
			//TODO revisar por cual de las constraints falló (si fue por la de producto, o la de alguno de sus hijos)
			throw new BusinessException(LegalPersonService.cLEGALPERSON_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE,bDataIntegrityViolationException);
		} catch (Exception ex){
			throw new BusinessException(ex.getMessage(),ex);
		}
		
		return mLegalPersonSaved;
	}
}
