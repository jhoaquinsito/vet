package backend.product.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import backend.exception.BusinessException;
import backend.product.Product;
import backend.product.ProductService;

/**
 * Un <code>BatchService</code> representa un conjunto de servicios relacionados a <code>Batch</code>.
 * 
 * Este conjunto de servicios tiene:
 * el repositorio de Batchos: <strong>BatchRepository</strong>.
 * 
 * @author ggorosito
 *
 */
@Service
public class BatchService {
	@Autowired private BatchRepository iBatchRepository;

	private static final String cBatch_DOESNT_EXIST_EXCEPTION_MESSAGE = "Intentaste obtener un Batcho que no existe.";
	
	/**
	 * Constructor.
	 */
	public BatchService() {
		super();
	}
	
	/**
	 * Método que permite obtener un Batcho a partir de su identificador.
	 * @param pId identificador del Batcho
	 * @return Batcho encontrado
	 * @throws BusinessException intentó obtener un Batcho eliminado lógicamente
	 */
	public Batch get(Long pId) throws BusinessException{
		Batch mStoredBatch = this.iBatchRepository.findOne(pId);
		
		// si es null, es porque no existe ningún Batcho con dicho id
		if (mStoredBatch == null) {
			throw new BusinessException(BatchService.cBatch_DOESNT_EXIST_EXCEPTION_MESSAGE);
		}

		
		return mStoredBatch;
	}
	
	
	/**
	 * Método que permite obtener la lista de productos de forma completa.
	 * @return Iterable<Batch> Lista de lotes.
	 * @throws BusinessException
	 */
	public Iterable<Batch> getAll() throws BusinessException {
		List<Batch> mResult = new ArrayList<Batch>();
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction 		= Direction.ASC;
			List<String> properties 	= new ArrayList<String>();
			properties.add("iIsoDueDate");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			Iterator<Batch> mIterator = this.iBatchRepository.findAll(sort).iterator();
	
			while (mIterator.hasNext()) {
				Batch mBatch = mIterator.next();
				if (mBatch.getProduct().isActive())
					mResult.add(mBatch);
			}
			// TODO catchear la excepción que corresponde y no una general
		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}
		
		//Usamos el nuevo findAll con el Sorting.
		return mResult;
	

	}
	
	/**
	 * Método PUBLICO que permite filtrar aquellos productos cuyos lotes (Batch) han expirado,
	 * o también expirarán dentro del rango de fecha provisto por la fecha de inicio mas la cantidad de días propuestos
	 * En formula:
	 * 
	 * Lotes / Lote.dueDate <= pBeginDate + pDaysToCheck
	 * 
	 * Si existe un lote dentro del producto que cae como "vencido" para esas condiciones, entonces se devuelve el producto con ese lote.
	 * Si ningun lote cae como vencido, entonces el producto no se devuelve.
	 * Y si hay algunos lotes que no caen y otros sí, solamente se devuelven aquellos lotes que sí caen como vencidos.
	 * 
	 * @param pBeginDate : Fecha de comienzo a considerar (usualmente se manda la fecha actual en el servidor)
	 * @param pDaysToCheck : Días a sumarle a la fecha para hacer el chequeo de vencimiento
	 * @return
	 * @throws BusinessException
	 */
	public List<Batch> getListByDueDate(Date pBeginDate, int pDaysToCheck)  throws BusinessException{
		
		List<Batch> mResult = new ArrayList<Batch>();
		
		try {
			//, traerá todos los lotes que tengan fecha de vencimiento <= fecha actual + X dias
			for (Batch mBatch : this.getAll()) {
				if(mBatch.isGoingToExpire(pBeginDate, pDaysToCheck))
					mResult.add(mBatch);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return mResult;
	}
}
