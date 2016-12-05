package backend.person;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.exception.BusinessException;
import backend.person.settlement.Settlement;

@Service
public class PersonService {
	private static final String cPERSON_DOESNT_EXIST_EXCEPTION_MESSAGE = "La persona no existe";
	
	@Autowired private PersonRepository iPersonRepository;
	
	/**
	 * Constructor.
	 */
	public PersonService() {
		super();
	}
	
	/**
	 * Método que permite obtener una Persona a partir de su identificador.
	 * @param pId identificador de la persona
	 * @return persona encontrado
	 * @throws BusinessException intentó obtener un producto eliminado lógicamente
	 */
	public Person get(Long pId) throws BusinessException{
		Person mStoredPerson = this.iPersonRepository.findOne(pId);
		
		// si es null, es porque no existe ningún producto con dicho id
		if (mStoredPerson == null) {
			throw new BusinessException(PersonService.cPERSON_DOESNT_EXIST_EXCEPTION_MESSAGE);
		}
		
		return mStoredPerson;
	}
	
	public Iterable<Person> getAll() {
		Iterable<Person> mPeople = this.iPersonRepository.findAll();
		
		return mPeople; 
	}
	
	public Set<Settlement> getClientSettlements(Long pPersonId) throws BusinessException{
		return this.get(pPersonId).getSettlements();
	}
	
	public boolean existsActiveByName(String pPersonName){
		return this.iPersonRepository.existsActiveByName(pPersonName);
	}
	
	/**
	 * Método que permite eliminar una persona legal a partir de su identificador.
	 * La eliminación es de tipo lógica.
	 * @param pId identificador de la persona a eliminar
	 * @throws BusinessException errores de negocio al intentar realizar la operación
	 */
	public void delete(Long pId) throws BusinessException {
		// busco el persona legal por id
		Person mPersonToDelete = this.get(pId);
	
		mPersonToDelete.setActive(false);
		
		// almaceno el persona legal desactivado y sin los lotes
		this.iPersonRepository.save(mPersonToDelete);
		
	}
	
	public Person save(Person pPersonToSave) {
		Person mPersonSaved = null;
		
		mPersonSaved = this.iPersonRepository.save(pPersonToSave);
		
		return mPersonSaved;
	}
	
}
