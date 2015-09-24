package backend.product;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;


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
	 * @throws BusinessException 
	 */
	public Product save(Product pProductToSave) throws BusinessException {
		// valido si el producto tiene datos válidos
		this.validate(pProductToSave);
		
		// guardo el producto
		Product mProductSaved = this.iProductRepository.save(pProductToSave);
		
		return mProductSaved;
	}
	
	/**
	 * Metodo que permite validar un <strong>producto</strong>, antes de enviarlo a la capa de Repository
	 * Estas validaciones corresponden directamente con el modelo.
	 * 
	 * @param Product :
	 * @return void
	 * @throws BusinessException - Una excepcion de negocio con el detalle del error.
	 */
	private void validate(Product pProduct) throws BusinessException{
		// TODO eliminar hardcode strings
		// TODO usar convenciones para las variables
		String friendlyMessage = "Producto NO valido: ";
		
		//(String pClassName, String pMethodName, String pExMessage, String pRequestUrl, HttpStatus pStatusCode) {
		
		if(pProduct.getName().length() == 0){
			throw new BusinessException("ProductService","ProductService", "validate",  friendlyMessage + "  Nombre vacio ", HttpStatus.CONFLICT);
		}
		if(pProduct.getName().length() > 100){
			throw new BusinessException("ProductService","ProductService", "validate",  friendlyMessage + " Nombre excede el limite de caracteres (100) ", HttpStatus.CONFLICT);
		}
		
		if(pProduct.getCategory() == null){
			throw new BusinessException("ProductService","ProductService", "validate",  friendlyMessage + " Categoria vacia ", HttpStatus.CONFLICT);
		}
		
		if(pProduct.getDrugs().isEmpty() ){
			throw new BusinessException("ProductService","ProductService", "validate",  friendlyMessage + " No ha especificado ninguna Droga  ", HttpStatus.CONFLICT);
		}
		
		if(pProduct.getManufacturer() == null){
			throw new BusinessException("ProductService","ProductService", "validate",  friendlyMessage + " Manufacturero vacio ", HttpStatus.CONFLICT);
		}
		
		if(pProduct.getMeasureUnit() == null){
			throw new BusinessException("ProductService","ProductService", "validate",  friendlyMessage + "  Unidad de Medida vacia  ", HttpStatus.CONFLICT);
		}
		
		if(pProduct.getPresentation() == null){
			throw new BusinessException("ProductService","ProductService", "validate",  friendlyMessage + " Presnetacion vacio ", HttpStatus.CONFLICT);
			
		}
	}

}
