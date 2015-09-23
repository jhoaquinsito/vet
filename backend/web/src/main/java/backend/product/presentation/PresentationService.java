package backend.product.presentation;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import backend.core.ApplicationConfiguration;

/**
 * Un <code>PresentationService</code> representa un conjunto de servicios relacionados a <code>Presentation</code>.
 * 
 * Este conjunto de servicios tiene:
 * el repositorio de presentaciones: <strong>PresentationRepository</strong>.
 * 
 */
public class PresentationService {

	private PresentationRepository iPresentationRepository;
	
	/**
	 * Constructor.
	 */
	public PresentationService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iPresentationRepository = (PresentationRepository) mAppContext.getBean(PresentationRepository.class);
	}
	
	/**
	 * Método que permite leer todas las presentaciones. 
	 * 
	 * @return lista de presentaciones
	 */
	public Iterable<Presentation> getAll() {
		return this.iPresentationRepository.findAll();
	}

}
