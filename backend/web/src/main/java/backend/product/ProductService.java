package backend.product;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
	private static final String cDELETED_PRODUCT_EXCEPTION_MESSAGE = "Intentaste obtener un producto eliminado lógicamente.";
	private static final String cPRODUCT_DOESNT_EXIST_EXCEPTION_MESSAGE = "Intentaste obtener un producto que no existe.";
	private static final String cCANNOT_SAVE_PRODUCT_EXCEPTION_MESSAGE = "El producto que intentas guardar no se puede guardar: o no existe o está eliminado lógicamente.";
	
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
		// verifico que el producto que se intenta guardar no esté eliminado
		// o que traiga un identificador que no existe
		if (pProductToSave.getId() != null){
			try{
				this.get(pProductToSave.getId());
			} catch (BusinessException bBusinessException){
				// o no existe o sino está eliminado
				throw new BusinessException(ProductService.cCANNOT_SAVE_PRODUCT_EXCEPTION_MESSAGE, bBusinessException);
			}
		}
		
		// valido si el producto tiene datos válidos
		this.validate(pProductToSave);
		
		// asocio el producto a sus lotes (si tiene)
		if (pProductToSave.getBatches() != null){
			for (Batch bBatch : pProductToSave.getBatches()){
				bBatch.setProduct(pProductToSave);
			}
		}
		
		// marco el producto como activo
		pProductToSave.setActive(true);
		
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
			throw new BusinessException(ProductService.cNULL_NAME_EXCEPTION_MESSAGE);
		}
		// valido que el nombre del producto no esté vacío
		if(pProduct.getName().length() == 0){
			throw new BusinessException(ProductService.cEMPTY_NAME_EXCEPTION_MESSAGE);
		}
		// valido que el nombre del producto sea menor a 100 caracteres
		if(pProduct.getName().length() > 100){
			throw new BusinessException(ProductService.cLONG_NAME_EXCEPTION_MESSAGE);
		}
		// valido que la unidad de medida no sea null
		if(pProduct.getMeasureUnit() == null){
			throw new BusinessException(ProductService.cNULL_MEASURE_UNIT_EXCEPTION_MESSAGE);
		}
	}

	/**
	 * Método que permite obtener un producto a partir de su identificador.
	 * @param pId identificador del producto
	 * @return producto encontrado
	 * @throws BusinessException intentó obtener un producto eliminado lógicamente
	 */
	public Product get(Long pId) throws BusinessException{
		Product mStoredProduct = this.iProductRepository.findOne(pId);
		
		// si es null, es porque no existe ningún producto con dicho id
		if (mStoredProduct == null) {
			throw new BusinessException(ProductService.cPRODUCT_DOESNT_EXIST_EXCEPTION_MESSAGE);
		}

		// checkeo si el producto NO está activo
		if (!mStoredProduct.isActive()){
			throw new BusinessException(ProductService.cDELETED_PRODUCT_EXCEPTION_MESSAGE);
		}
		
		return mStoredProduct;
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
		this.iProductRepository.save(mProductToDelete);
		
	}

}