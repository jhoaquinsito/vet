package backend.product;


import java.util.HashSet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.product.batch.Batch;
import backend.utils.EntityValidator;


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
	private EntityValidator iEntityValidator;

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
		this.iEntityValidator = new EntityValidator();
		
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
		
		//TODO esto debería estar en otra capa anterior (al mapear DTO con domain)
		// asocio el producto a sus lotes (si tiene)
		if (pProductToSave.getBatches() != null){
			for (Batch bBatch : pProductToSave.getBatches()){
				bBatch.setProduct(pProductToSave);
			}
		} else {
			pProductToSave.setBatches(new HashSet<Batch>());
		}
		
		// marco el producto como activo
		pProductToSave.setActive(true);
		
		//valido la entidad
		this.iEntityValidator.validate(pProductToSave);
		
		// guardo el producto
		Product mProductSaved = this.iProductRepository.save(pProductToSave);
		
		return mProductSaved;
	}

	/**
	 * Método que permite obtener la lista de productos de forma completa.
	 * @return Iterable<Product> Lista de productos.
	 * @throws BusinessException
	 */
	public Iterable<Product> getAll() throws BusinessException {
		return this.iProductRepository.findAll();
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