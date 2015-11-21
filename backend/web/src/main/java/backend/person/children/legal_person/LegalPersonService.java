package backend.person.children.legal_person;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

public class LegalPersonService {
	
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
		return this.iLegalPersonRepository.findAll();
	}
}
