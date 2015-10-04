package backend.product.manufacturer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;

import backend.core.ApplicationConfiguration;
import backend.exception.BusinessException;

/**
 * Un <code>ManufacturerService</code> representa un conjunto de servicios relacionados a <code>Manufacturer</code>.
 * 
 * Este conjunto de servicios tiene:
 * el repositorio de productos: <strong>ManufacturerRepository</strong>.
 * 
 * @author genesis
 *
 */

public class ManufacturerService {
	
	private ManufacturerRepository iManufacturerRepository;
	
	/**
	 * Constructor.
	 */
	public ManufacturerService() {
		super();
		// obtengo el repositorio desde el contexto de la applicación
		@SuppressWarnings("resource")
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		this.iManufacturerRepository = (ManufacturerRepository) mAppContext.getBean(ManufacturerRepository.class);
	}
	
	/**
	 * Método que permite guardar laboratorios. Puede ser un laboratorio nuevo (creación) o
	 * un laboratorio existente que está modificado (actualización)
	 * @param pManufacturerToSave producto que se desea guardar
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
	
	public List<Manufacturer> getByName(String pManufacturerName) throws BusinessException {
		List<Manufacturer> mResult = new ArrayList<Manufacturer>();
		
		try {
			Iterator<Manufacturer> mIterator = this.iManufacturerRepository.findAll().iterator();
			
			while(mIterator.hasNext()){
				Manufacturer mManufacturer = mIterator.next();
				if(mManufacturer.getName().toLowerCase().equalsIgnoreCase(pManufacturerName.toLowerCase()))
					mResult.add(mManufacturer);
			}
		// TODO catchear la excepción que corresponde y no la más general de todas
		} catch (Exception bException) {
			// TODO usar constantes y dar un mensaje mas amigable y que sirva
			throw new BusinessException(bException.getMessage(), bException);
		}
		
		
		return mResult;
		
	}
	
	/**
	 * Metodo que permite validar una <strong>Laboratorio</strong>, antes de enviarlo a la capa de Repository
	 * Estas validaciones corresponden directamente con el modelo.
	 * 
	 * @param Manufacturer : Categoria a Validar
	 * @return void
	 * @throws BusinessException - Una excepcion de negocio con el detalle del error.
	 */
	private void validate(Manufacturer pManufacturer) throws BusinessException{
		// TODO revisar convenciones de código
		// TODO no hardcodear, usar constantes
		String friendlyMessage = "Manufacturer NO valido: ";
		
		//(String pClassName, String pMethodName, String pExMessage, String pRequestUrl, HttpStatus pStatusCode) {
		
		if(pManufacturer.getName().length() == 0){
			// TODO no hardcodear, usar constantes
			throw new BusinessException(friendlyMessage + " Nombre vacio ");
		}
		if(pManufacturer.getName().length() > 30){
			// TODO no hardcodear, usar constantes
			throw new BusinessException(friendlyMessage + " Nombre excede el limite de caracteres (30) ");
		}
		if(!this.getByName(pManufacturer.getName()).isEmpty()){
			// TODO no hardcodear, usar constantes
			throw new BusinessException(friendlyMessage + " Ya existe un manufacturer bajo el mismo nombre ( " + pManufacturer.getName() + " )");
		}
		
		
	}

	public Iterable<Manufacturer> getAll() {
		return this.iManufacturerRepository.findAll();
	}
	
	public Manufacturer getById(long id){
		return this.iManufacturerRepository.findOne(id);
	}
	
	
}
