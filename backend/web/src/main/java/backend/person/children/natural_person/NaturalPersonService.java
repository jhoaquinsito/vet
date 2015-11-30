package backend.person.children.natural_person;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

public class NaturalPersonService {
	
	private NaturalPersonRepository iNaturalPersonRepository;
	
	/**
	 * Constructor.
	 */
	public NaturalPersonService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iNaturalPersonRepository = mAppContext.getBean(NaturalPersonRepository.class);
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

}
