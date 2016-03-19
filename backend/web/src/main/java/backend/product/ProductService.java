package backend.product;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.dao.DataIntegrityViolationException;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.exception.ExceptionUtils;
import backend.person.children.legal_person.LegalPersonService;
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
	private static final String cPRODUCT_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "Hubo un problema con alguna de las restricciones de la base de datos. Muy probablemente el nombre de un producto, o alguno de sus hijos, violó una restricción unique.";
	private static final String cCANNOT_SAVE_PRODUCT_EXCEPTION_MESSAGE = "El producto que intentas guardar no se puede guardar: o no existe o está eliminado lógicamente.";
	
	/**
	 * Constructor.
	 */
	public ProductService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		@SuppressWarnings("resource")
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
		

			
		// marco el producto como activo
		pProductToSave.setActive(true);
		
		//valido la entidad
		this.iEntityValidator.validate(pProductToSave);
		
		// guardo el producto
		Product mProductSaved = this.tryToSave(pProductToSave);
		
		
		return mProductSaved;
	}

	/**
	 * Método que permite obtener la lista de productos de forma completa.
	 * @return Iterable<Product> Lista de productos.
	 * @throws BusinessException
	 */
	public Iterable<Product> getAll() throws BusinessException {
		List<Product> mResult = new ArrayList<Product>();
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iName");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			Iterator<Product> mIterator = this.iProductRepository.findAll(sort).iterator();
	
			while (mIterator.hasNext()) {
				Product mProduct = mIterator.next();
				if (mProduct.isActive())
					mResult.add(mProduct);
			}
			// TODO catchear la excepción que corresponde y no una general
		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}
		
		//Usamos el nuevo findAll con el Sorting.
		return mResult;
	

	}
	
	/***
	 * Metodo que permite obtener la lista de productos por nombre.
	 * 
	 * @param pProductName
	 * 						Parametro para comprar productos por similitud.
	 * @return
	 * @throws BusinessException
	 * 						Excepcion de negocio.
	 */
	public Iterable<Product> getProductListByName(String pProductName) throws BusinessException{
		List<Product> mResultList 	= new ArrayList<Product>();
		List<Product> mProductList 	= new ArrayList<Product>();
		
		try {
			Iterator<Product> mIterator = this.getAll().iterator();
			
			while (mIterator.hasNext()) {
				Product mProduct = mIterator.next();
				if (mProduct.isActive())
					mProductList.add(mProduct);
			}
			
			mResultList.addAll(this.filterProductByName(mProductList, pProductName));
			
		}catch (Exception e) {
			
			throw new BusinessException(e.getMessage());
		}
		
		return mResultList;
	}
		
	/**
	 * Metodo privado que permite filtrar un iterador de productos
	 * usando como filtro el codigo de sus lotes (Batch), y un fragmento de codigo como entrada.
	 * 
	 * Si alguno de los batch.getCode() contiene a la secuencia entonces ese producto entra en la lista final.
	 * 
	 * @param pProductIterator
	 * 			Iterador de productos, puede estar previamente filtrado o no.
	 * @param pName
	 * 			Secuencia de nombre de un producto, puede ser un fragmento o completo.
	 * @return
	 */
	private List<Product> filterProductByName(List<Product> pProductIterator, String pName)  throws BusinessException{
		List<Product> mResult = new ArrayList<Product>();
		try {
			for (Product mProduct : pProductIterator) {
				
				if(mProduct.getName().toLowerCase().contains(pName.toLowerCase())){
					mResult.add(mProduct);
					continue;
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return mResult;
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
	
	/**
	 * Método que guarda un producto en la base de datos.
	 * 
	 * @param pProductToSave producto a guardar
	 * @return producto guardado o null, si hubo un error y no se pudo guardar
	 * 
	 * @throws BusinessException Excepcion producida si el producto no se pudo
	 * guardar por problemas de restricciones en las tablas de la base de datos.
	 */
	private Product tryToSave(Product pProductToSave) throws BusinessException {
		Product mProductSaved = null;
		
		try {
			mProductSaved = this.iProductRepository.save(pProductToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){

			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(ProductService.cPRODUCT_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(ProductService.cPRODUCT_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE,bDataIntegrityViolationException);
			
		}
		
		return mProductSaved;
	}

}