package backend.product.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.product.ProductRepository;
import backend.product.ProductService;

/**
 * Un <code>ProductService</code> representa un conjunto de servicios relacionados a <code>Product</code>.
 * 
 * Este conjunto de servicios tiene:
 * el repositorio de productos: <strong>ProductRepository</strong>.
 * 
 * @author Genesis
 *
 */
public class BatchService {

	private BatchRepository iBatchRepository;

	private static final String cEMPTY_BATCH_EXCEPTION_MESSAGE = "Lote NO valido: Lote nulo  ";
	private static final String cEMPTY_STOCK_EXCEPTION_MESSAGE = "Lote NO valido: Stock sin valor ";
	private static final String cRANGE_STOCK_EXCEPTION_MESSAGE = "Lote NO valido: Stock fuera de rango ";
	private static final String cEMPTY_DUEDATE_EXCEPTION_MESSAGE = "Lote NO valido: DueDate sin valor ";
	
	/**
	 * Constructor.
	 */
	public BatchService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iBatchRepository = mAppContext.getBean(BatchRepository.class);
	}
	
	/**
	 * Metodo que permite validar un <strong>Lote</strong>, antes de enviarlo a la capa de Repository
	 * Estas validaciones corresponden directamente con el modelo.
	 * 
	 * @param pBatch : Lote a ser validado.
	 * @return void
	 * @throws BusinessException - Una excepcion de negocio con el detalle del error.
	 */
	public static boolean validate(Batch pBatch) throws BusinessException
	{
		//valido primeramente que el Lote no sea nulo.
		if(pBatch == null){
			throw new BusinessException(BatchService.cEMPTY_BATCH_EXCEPTION_MESSAGE);
		}
			
		
		// valido que el stock del producto este entre 0-100
		if(pBatch.getStock() == null){
			throw new BusinessException(BatchService.cEMPTY_STOCK_EXCEPTION_MESSAGE);
		}
		// valido que el stock del producto este entre 0-100
		if(pBatch.getStock().intValue() < 0  || pBatch.getStock().intValue() > 100){
			throw new BusinessException(BatchService.cRANGE_STOCK_EXCEPTION_MESSAGE);
		}
		
		if(pBatch.getDueDate() == null){
			throw new BusinessException(BatchService.cEMPTY_DUEDATE_EXCEPTION_MESSAGE);
		}
				
		
		
		return true;
		
	
	}
}
