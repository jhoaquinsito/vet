package backend.sale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.exception.ExceptionUtils;
import backend.product.presentation.PresentationService;
import backend.sale.Sale;
import backend.sale.SaleRepository;
import backend.sale.SaleService;
import backend.saleline.SaleLine;
import backend.utils.EntityValidator;

//TODO revisar si no hay que usar inyección de dependencias acá o
//bien aplicar Singleton pattern
/**
* Un <code>SaleService</code> representa un conjunto de servicios relacionados a <code>Sale</code>.
* 
* Este conjunto de servicios tiene:
* el repositorio de ventas: <strong>SaleRepository</strong>.
* 
* @author ggorosito
*
*/
public class SaleService {
	
	private SaleRepository iSaleRepository;
	private EntityValidator iEntityValidator;

	
	/**
	 * Constructor.
	 */
	public SaleService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		@SuppressWarnings("resource")
		ApplicationContext mAppContext 	= new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iSaleRepository 			= mAppContext.getBean(SaleRepository.class);
		this.iEntityValidator 			= new EntityValidator();
		
	}
	
	/**
	 * Método que permite guardar ventas. Puede ser un venta nuevo (creación) o
	 * un venta existente que esté modificado (actualización).
	 * 
	 * @param pSaleToSave venta que se desea guardar
	 * @return venta tal cual quedó guardado
	 * @throws BusinessException 
	 */
	public Sale save(Sale pSaleToSave) throws BusinessException {
		// verifico que el venta que se intenta guardar no esté eliminado
		// o que traiga un identificador que no existe
		if (pSaleToSave.getId() != null){
			try{
				this.get(pSaleToSave.getId());
			} catch (BusinessException bBusinessException){
				// o no existe o sino está eliminado
				throw new BusinessException(SaleCons.cCANNOT_SAVE_SALE_EXCEPTION_MESSAGE, bBusinessException);
			}
		}
		
		// asocio el venta a sus lotes (si tiene)
		if (pSaleToSave.getSaleLines() != null  || pSaleToSave.getSaleLines().size() == 0){
			for (SaleLine bSaleLine : pSaleToSave.getSaleLines()){
				if(bSaleLine.getProduct() == null) throw new BusinessException(SaleCons.cCANNOT_SAVE_WITHOUT_SALELINE_WITHOUT_PRODUCT_EXCEPTION_MESSAGE);
				bSaleLine.setUnit_Price(bSaleLine.getProduct().getUnitPrice());
				bSaleLine.setSale(pSaleToSave);
			}
		} else {
			throw new BusinessException(SaleCons.cCANNOT_SAVE_WITHOUT_SALELINE_EXCEPTION_MESSAGE);
		}
		
		
		//valido la entidad
		this.iEntityValidator.validate(pSaleToSave);
		
		// guardo el venta
		Sale mSaleSaved = this.tryToSave(pSaleToSave);
		
		
		return mSaleSaved;
	}

	/**
	 * Método que permite obtener la lista de ventas de forma completa.
	 * @return Iterable<Sale> Lista de ventas.
	 * @throws BusinessException
	 */
	public Iterable<Sale> getAll() throws BusinessException {
		List<Sale> mResult = new ArrayList<Sale>();
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iDate");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			Iterator<Sale> mIterator = this.iSaleRepository.findAll(sort).iterator();
	
			while (mIterator.hasNext()) {
				Sale mSale = mIterator.next();
				//if (mSale.isActive())
					mResult.add(mSale);
			}
			// TODO catchear la excepción que corresponde y no una general
		} catch (Exception e) {
			throw new BusinessException(SaleCons.cCANNOT_GET_SALES);
		}
		
		//Usamos el nuevo findAll con el Sorting.
		return mResult;
	}
	
	/**
	 * Método que permite obtener una venta a partir de su identificador.
	 * @param pId identificador del venta
	 * @return venta encontrada
	 * @throws BusinessException intentó obtener un venta eliminado lógicamente
	 */
	public Sale get(Long pId) throws BusinessException{
		Sale mStoredSale = this.iSaleRepository.findOne(pId);
		
		// si es null, es porque no existe ningún venta con dicho id
		if (mStoredSale == null) {
			throw new BusinessException(SaleCons.cSALE_DOESNT_EXIST_EXCEPTION_MESSAGE);
		}

		// checkeo si el venta NO está activo
		//if (!mStoredSale.isActive()){
		//	throw new BusinessException(SaleCons.cDELETED_SALE_EXCEPTION_MESSAGE);
		//}
		
		return mStoredSale;
	}
	
	/**
	 * Método que permite eliminar un venta a partir de su identificador.
	 * Al eliminar el venta, sus los lotes asociados son eliminados físicamente.
	 * @param pId identificador del venta a eliminar
	 * @throws BusinessException errores de negocio al intentar realizar la operación
	 */
	public void delete(Long pId) throws BusinessException{
		
		// busco el venta por id
		Sale mSaleToDelete = this.get(pId);
		
		// limpio la lista de lotes asociados
		mSaleToDelete.getSaleLines().clear();
		
		// almaceno el venta desactivado y sin los lotes
		this.iSaleRepository.save(mSaleToDelete);
		
	}
	
	/**
	 * Método que guarda un venta en la base de datos.
	 * 
	 * @param pSaleToSave venta a guardar
	 * @return venta guardado o null, si hubo un error y no se pudo guardar
	 * 
	 * @throws BusinessException Excepcion producida si el venta no se pudo
	 * guardar por problemas de restricciones en las tablas de la base de datos.
	 */
	private Sale tryToSave(Sale pSaleToSave) throws BusinessException {
		Sale mSaleSaved = null;
		
		try {
			mSaleSaved = this.iSaleRepository.save(pSaleToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){
			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(SaleCons.cSALE_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE  + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(SaleCons.cSALE_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE ,bDataIntegrityViolationException);
			
		}
		
		return mSaleSaved;
	}
	

	/**
	 * Devuelve las ventas correspondientes a un cliente determinado.
	 * @param pClientId el identificador del cliente del que se quieren recuperar las ventas.
	 * @return mClientSales las ventas del cliente.
	 * @throws BusinessException
	 */
	public List<Sale> getDueSalesByClientId(Long pClientId) throws BusinessException{
		List<Sale> mClientDueSales = new ArrayList<Sale>();
		Iterable<Sale> mSales = this.getAll();
		Iterator<Sale> mSalesIterator = mSales.iterator();
			
		while(mSalesIterator.hasNext()){
			Sale bSale = mSalesIterator.next();
			if((bSale.getPerson().getId() == pClientId) && (!bSale.isPaied_out())){
				mClientDueSales.add(bSale);
			}
		}
		
		return mClientDueSales;		
	}
	
	/**
	 * Coloca todas las ventas adeudadas de un cliente como pagadas.
	 * @param pClientId identificador del cliente del que se quieren pagar las ventas
	 * @throws BusinessException
	 */
	public void payClientSales(Long pClientId) throws BusinessException{
		Iterator<Sale> mSalesIterator = this.getDueSalesByClientId(pClientId).iterator();
		
		while(mSalesIterator.hasNext()){
			Sale bSale = mSalesIterator.next();
			bSale.setPaied_out(true);
			this.iSaleRepository.save(bSale);
		}
	}
}