package backend.product.manufacturer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import backend.exception.BusinessException;
import backend.exception.ExceptionUtils;
import backend.utils.EntityValidator;

/**
 * Un <code>ManufacturerService</code> representa un conjunto de servicios
 * relacionados a <code>Manufacturer</code>.
 * 
 * Este conjunto de servicios tiene: el repositorio de laboratorios:
 * <strong>ManufacturerRepository</strong>.
 * 
 * @author genesis
 *
 */
@Service
public class ManufacturerService {

	@Autowired private ManufacturerRepository iManufacturerRepository;
	private EntityValidator iEntityValidator;
	private static final String cMANUFACTURER_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE = "Hubo un problema con alguna de las restricciones de la base de datos. Muy probablemente el nombre de un Laboratorio, o alguno de sus hijos, violó una restricción unique.";
	/**
	 * Constructor.
	 */
	public ManufacturerService() {
		super();
		this.iEntityValidator = new EntityValidator();
	}

	/**
	 * Método que permite guardar laboratorios. Puede ser un laboratorio nuevo
	 * (creación) o un laboratorio existente que está modificado (actualización)
	 * 
	 * @param pManufacturerToSave
	 *            producto que se desea guardar
	 * @return Manufacturer tal cual quedó guardada
	 * @throws BusinessException
	 */
	public Manufacturer save(Manufacturer pManufacturerToSave) throws BusinessException {
		
		Manufacturer mManufacturerSaved = null;
		
		try {
		// Valido si el producto tiene datos válidos
		this.iEntityValidator.validate(pManufacturerToSave);

		// Guardo el laboratorio
		mManufacturerSaved = this.iManufacturerRepository.save(pManufacturerToSave);

		
		} catch (DataIntegrityViolationException bDataIntegrityViolationException){

			String mCauseMessage = ExceptionUtils.getDataIntegrityViolationExceptionCause(bDataIntegrityViolationException);
			
			if(mCauseMessage != null && mCauseMessage.length() > 0)
				throw new BusinessException(ManufacturerService.cMANUFACTURER_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE  + "\n" +mCauseMessage,bDataIntegrityViolationException);
			else
				throw new BusinessException(ManufacturerService.cMANUFACTURER_TABLE_CONSTRAINT_VIOLATED_EXCEPTION_MESSAGE ,bDataIntegrityViolationException);
			
		}
		return mManufacturerSaved;
	}

	// TODO comentar este método
	public List<Manufacturer> getByName(String pManufacturerName) throws BusinessException {
		List<Manufacturer> mResult = new ArrayList<Manufacturer>();

		try {
			Iterator<Manufacturer> mIterator = this.iManufacturerRepository.findAll().iterator();

			while (mIterator.hasNext()) {
				Manufacturer mManufacturer = mIterator.next();
				if (mManufacturer.getName().toLowerCase().equalsIgnoreCase(pManufacturerName.toLowerCase()))
					mResult.add(mManufacturer);
			}
			// TODO catchear la excepción que corresponde y no la más general de
			// todas
		} catch (Exception bException) {
			// TODO usar constantes y dar un mensaje mas amigable y que sirva
			throw new BusinessException(bException.getMessage(), bException);
		}

		return mResult;

	}

	// TODO para que sirve esto? refactorizarlo y documentarlo
	public Iterable<Manufacturer> getAll() throws BusinessException {
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iName");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			return this.iManufacturerRepository.findAll(sort);
	

		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}

	}

}
