package backend.product.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.exception.BusinessException;

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
}
