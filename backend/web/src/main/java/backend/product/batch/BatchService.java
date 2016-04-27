package backend.product.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.utils.EntityValidator;

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
	private EntityValidator iEntityValidator;
	
	
	private static final String cDELETED_Batch_EXCEPTION_MESSAGE = "Intentaste obtener un Batcho eliminado lógicamente.";
	private static final String cBatch_DOESNT_EXIST_EXCEPTION_MESSAGE = "Intentaste obtener un Batcho que no existe.";
	private static final String cBatch_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "Hubo un problema con alguna de las restricciones de la base de datos. Muy probablemente el nombre de un Batcho, o alguno de sus hijos, violó una restricción unique.";
	private static final String cCANNOT_SAVE_Batch_EXCEPTION_MESSAGE = "El Batcho que intentas guardar no se puede guardar: o no existe o está eliminado lógicamente.";
	
	/**
	 * Constructor.
	 */
	public BatchService() {
		super();
		this.iEntityValidator = new EntityValidator();
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
