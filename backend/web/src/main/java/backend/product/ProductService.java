package backend.product;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import backend.core.ApplicationConfiguration;


// TODO revisar si no hay que usar inyección de dependencias acá o
// bien aplicar Singleton pattern
/**
 * Un <code>ProductService</code> representa un conjunto de servicios relacionados a <code>Product</code>.
 * 
 * Este conjunto de servicios tiene:
 * el repositorio de productos: <strong>ProductRepository</strong>.
 * 
 * @author tomas
 *
 */
public class ProductService {
	
	private ProductRepository iProductRepository;
	
	/**
	 * Constructor.
	 */
	public ProductService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iProductRepository = (ProductRepository) mAppContext.getBean(ProductRepository.class);
	}
	
	/**
	 * Método que permite guardar productos. Puede ser un producto nuevo (creación) o
	 * un producto existente que esté modificado (actualización).
	 * 
	 * @param pProductToSave producto que se desea guardar
	 * @return producto tal cual quedó guardado
	 */
	public Product save(Product pProductToSave) {
		//TODO - Metodo private bool validate(Product pProductToSave) throws Exception
		return this.iProductRepository.save(pProductToSave);
	}

}
