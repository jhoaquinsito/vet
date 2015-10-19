package backend.product.manufacturer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

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

public class ManufacturerService {

	private ManufacturerRepository iManufacturerRepository;
	private EntityValidator iEntityValidator;
	// constantes para mensajes de excepciones:
	private static final String cEXISTING_NAME_EXCEPTION_MESSAGE = "Laboratorio no válido: el nombre ya existe en la base de datos.";

	/**
	 * Constructor.
	 */
	public ManufacturerService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		@SuppressWarnings("resource")
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iManufacturerRepository = mAppContext.getBean(ManufacturerRepository.class);
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
		// Valido si el producto tiene datos válidos
		this.iEntityValidator.validate(pManufacturerToSave);

		// Guardo el laboratorio
		Manufacturer mManufacturerSaved = this.iManufacturerRepository.save(pManufacturerToSave);

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
	public Iterable<Manufacturer> getAll() {
		return this.iManufacturerRepository.findAll();
	}

}
