package backend.saleline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort.Direction;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;
import backend.utils.EntityValidator;

/**
* Un <code>SaleLineService</code> representa un conjunto de servicios relacionados a <code>SaleLine</code>.
* 
* Este conjunto de servicios tiene:
* el repositorio de ventas: <strong>SaleLineRepository</strong>.
* 
* @author ggorosito
*
*/
public class SaleLineService {
	private SaleLineRepository iSaleLineRepository;
	private EntityValidator iEntityValidator;

	
	/**
	 * Constructor.
	 */
	public SaleLineService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		@SuppressWarnings("resource")
		ApplicationContext mAppContext 	= new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iSaleLineRepository 			= mAppContext.getBean(SaleLineRepository.class);
		this.iEntityValidator 			= new EntityValidator();
		
	}
	
	/**
	 * Método que permite guardar ventas. Puede ser un venta nuevo (creación) o
	 * un venta existente que esté modificado (actualización).
	 * 
	 * @param pSaleLineToSave venta que se desea guardar
	 * @return venta tal cual quedó guardado
	 * @throws BusinessException 
	 */
	public SaleLine save(SaleLine pSaleLineToSave) throws BusinessException {
		// verifico que el venta que se intenta guardar no esté eliminado
		// o que traiga un identificador que no existe
		if (pSaleLineToSave.getId() != null){
			try{
				this.get(pSaleLineToSave.getId());
			} catch (BusinessException bBusinessException){
				// o no existe o sino está eliminado
				throw new BusinessException(SaleLineCons.cCANNOT_SAVE_SALELINE_EXCEPTION_MESSAGE, bBusinessException);
			}
		}
	
		
		//valido la entidad
		this.iEntityValidator.validate(pSaleLineToSave);
		
		// guardo el venta
		SaleLine mSaleLineSaved = this.tryToSave(pSaleLineToSave);
		
		
		return mSaleLineSaved;
	}

	/**
	 * Método que permite obtener la lista de ventas de forma completa.
	 * @return Iterable<SaleLine> Lista de ventas.
	 * @throws BusinessException
	 */
	public Iterable<SaleLine> getAll() throws BusinessException {
		List<SaleLine> mResult = new ArrayList<SaleLine>();
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			@SuppressWarnings("unused")
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iSalelineId");
			//Creamos el objeto Sort para pasarle al query.
			//Sort sort = new Sort(direction,properties);
		
			Iterator<SaleLine> mIterator = this.iSaleLineRepository.findAll().iterator();//(sort).iterator();
	
			while (mIterator.hasNext()) {
				SaleLine mSaleLine = mIterator.next();
				//if (mSaleLine.isActive())
					mResult.add(mSaleLine);
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
	 * Método que permite obtener un venta a partir de su identificador.
	 * @param pId identificador del venta
	 * @return venta encontrado
	 * @throws BusinessException intentó obtener un venta eliminado lógicamente
	 */
	public SaleLine get(Long pId) throws BusinessException{
		SaleLine mStoredSaleLine = this.iSaleLineRepository.findOne(pId);
		
		// si es null, es porque no existe ningún venta con dicho id
		if (mStoredSaleLine == null) {
			throw new BusinessException(SaleLineCons.cSALELINE_DOESNT_EXIST_EXCEPTION_MESSAGE);
		}

		// checkeo si el venta NO está activo
		//if (!mStoredSaleLine.isActive()){
		//	throw new BusinessException(SaleLineCons.cDELETED_SALE_EXCEPTION_MESSAGE);
		//}
		
		return mStoredSaleLine;
	}
	
	/**
	 * Método que permite eliminar un venta a partir de su identificador.
	 * Al eliminar el venta, sus los lotes asociados son eliminados físicamente.
	 * @param pId identificador del venta a eliminar
	 * @throws BusinessException errores de negocio al intentar realizar la operación
	 */
	public void delete(Long pId) throws BusinessException{
		
		// busco el venta por id
		SaleLine mSaleLineToDelete = this.get(pId);
		
		
		// almaceno el venta desactivado y sin los lotes
		this.iSaleLineRepository.save(mSaleLineToDelete);
		
	}
	
	/**
	 * Método que guarda un venta en la base de datos.
	 * 
	 * @param pSaleLineToSave venta a guardar
	 * @return venta guardado o null, si hubo un error y no se pudo guardar
	 * 
	 * @throws BusinessException Excepcion producida si el venta no se pudo
	 * guardar por problemas de restricciones en las tablas de la base de datos.
	 */
	private SaleLine tryToSave(SaleLine pSaleLineToSave) throws BusinessException {
		SaleLine mSaleLineSaved = null;
		
		try {
			mSaleLineSaved = this.iSaleLineRepository.save(pSaleLineToSave);
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){
			//TODO revisar por cual de las constraints falló (si fue por la de venta, o la de alguno de sus hijos)
			throw new BusinessException(SaleLineCons.cSALELINE_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE,bDataIntegrityViolationException);
		}
		
		return mSaleLineSaved;
	}
}
