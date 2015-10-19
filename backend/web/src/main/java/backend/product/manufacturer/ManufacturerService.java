package backend.product.manufacturer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

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
	// constantes para mensajes de excepciones:
	private static final String cNULL_NAME_EXCEPTION_MESSAGE = "Laboratorio no válido: nombre sin valor.";
	private static final String cEMPTY_NAME_EXCEPTION_MESSAGE = "Laboratorio no válido: nombre vacío.";
	private static final String cLONG_NAME_EXCEPTION_MESSAGE = "Laboratorio no válido: nombre con más de 30 caracteres.";
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
		this.validate(pManufacturerToSave);

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

	/**
	 * Metodo que permite validar una <strong>Laboratorio</strong>, antes de
	 * enviarlo a la capa de Repository Estas validaciones corresponden
	 * directamente con el modelo.
	 * 
	 * @param Manufacturer
	 *            : Categoria a Validar
	 * @return void
	 * @throws BusinessException
	 *             - Una excepcion de negocio con el detalle del error.
	 */
	private void validate(Manufacturer pManufacturer) throws BusinessException {
		String mManufacturerName = pManufacturer.getName();

		if (pManufacturer.getName().length() == 0) {
			throw new BusinessException(ManufacturerService.cEMPTY_NAME_EXCEPTION_MESSAGE);
		}
		if (pManufacturer.getName().length() > 30) {
			throw new BusinessException(ManufacturerService.cLONG_NAME_EXCEPTION_MESSAGE);
		}
		if (!this.getByName(mManufacturerName).isEmpty()) {
			for (Manufacturer bManufacturer : this.getByName(mManufacturerName)) {
				if (bManufacturer.getId() != pManufacturer.getId()) {
					throw new BusinessException(ManufacturerService.cEXISTING_NAME_EXCEPTION_MESSAGE);
				}
			}
		}
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
