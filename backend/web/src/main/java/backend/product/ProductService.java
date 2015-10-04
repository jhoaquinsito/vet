package backend.product;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.product.batch.Batch;


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


	private static final String cNULL_NAME_EXCEPTION_MESSAGE = "Producto NO valido: Nombre sin valor ";
	private static final String cEMPTY_NAME_EXCEPTION_MESSAGE = "Producto NO valido: Nombre vacio ";
	private static final String cLONG_NAME_EXCEPTION_MESSAGE = "Producto NO valido: Nombre excede el limite de caracteres (100) ";
	private static final String cNULL_MEASURE_UNIT_EXCEPTION_MESSAGE = "Producto NO valido: Unidad de Medida vacia  ";
	
	/**
	 * Constructor.
	 */
	public ProductService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iProductRepository = mAppContext.getBean(ProductRepository.class);
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
		// valido que el nombre del producto no sea null
		if (pProduct.getName() == null){
			throw new BusinessException("ProductService","Producto no válido", "validate",  ProductService.cNULL_NAME_EXCEPTION_MESSAGE, HttpStatus.CONFLICT);
		}
		// valido que el nombre del producto no esté vacío
		if(pProduct.getName().length() == 0){
			throw new BusinessException("ProductService","Producto no válido", "validate",  ProductService.cEMPTY_NAME_EXCEPTION_MESSAGE, HttpStatus.CONFLICT);
		}
		// valido que el nombre del producto sea menor a 100 caracteres
		if(pProduct.getName().length() > 100){
			throw new BusinessException("ProductService","Producto no válido", "validate", ProductService.cLONG_NAME_EXCEPTION_MESSAGE, HttpStatus.CONFLICT);
		}
		// valido que la unidad de medida no sea null
		if(pProduct.getMeasureUnit() == null){
			throw new BusinessException("ProductService","Producto no válido", "validate", ProductService.cNULL_MEASURE_UNIT_EXCEPTION_MESSAGE, HttpStatus.CONFLICT);
		}
	}

	/**
	 * Método que permite obtener un producto a partir de su identificador.
	 * @param pId identificador del producto
	 * @return producto encontrado
	 */
	public Product get(Long pId){
		return this.iProductRepository.findOne(pId);
	}
	
	/**
	 * Método que permite eliminar un producto a partir de su identificador.
	 * Al eliminar el producto, sus los lotes asociados son eliminados físicamente.
	 * @param pId identificador del producto a eliminar
	 * @throws BusinessException errores de negocio al intentar realizar la operación
	 */
	public void delete(Long pId) throws BusinessException{
		
		// busco el producto por id
		Product mProductToDelete = this.get(pId);
		
		// desactivo el producto
		mProductToDelete.setActive(false);
		
		// limpio la lista de lotes asociados
		mProductToDelete.getBatches().clear();
		
		// almaceno el producto desactivado y sin los lotes
		this.save(mProductToDelete);
		
	}

}