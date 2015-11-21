package backend.person.children.real_person;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

public class RealPersonService {
	
	private RealPersonRepository iRealPersonRepository;
	
	/**
	 * Constructor.
	 */
	public RealPersonService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iRealPersonRepository = mAppContext.getBean(RealPersonRepository.class);
	}
	
	/**
	 * Método que permite obtener la lista de personas reales de forma
	 * completa.
	 * @return Iterable<RealPerson> Lista de personas reales.
	 * @throws BusinessException
	 */
	public Iterable<RealPerson> getAll() throws BusinessException {
		return this.iRealPersonRepository.findAll();
	}

}
