package backend.core;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import backend.exception.BusinessException;
import backend.exception.ErrorDTO;
import backend.person.PersonDTO;
import backend.person.children.natural_person.NaturalPerson;
import backend.person.children.natural_person.NaturalPersonDTO;
import backend.person.iva_category.IVACategoryDTO;
import backend.person.children.legal_person.LegalPersonDTO;
import backend.product.ProductDTO;
import backend.product.batch.BatchDTO;
import backend.product.category.CategoryDTO;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.PresentationDTO;
import backend.sale.SaleDTO;
import backend.utils.PrintItemDTO;
import backend.utils.ZebraPrintHelper;

/**
 * Este <code>Controlador</code> es el encargado de recibir los request desde la
 * interfaz, procesarlos, y devolver la respuesta al cliente.
 * 
 * @author genesis
 *
 */
@RestController
@RequestMapping("api/")
public class ApplicationRESTController {

	//=======================================================================================
	
	// PRODUCTOS
	
	/**
	 * Metodo API que permite crear un Producto.
	 * 
	 * @param ProductDTO: datos del producto a crear
	 * @return Long : Identificador del nuevo producto en la BD.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "product", method = RequestMethod.POST)
	public Long createProduct(@RequestBody ProductDTO product) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.saveProduct(product);

		return mId;
	}

	/**
	 * Metodo API que permite recuperar la lista de los distintos Product.
	 * 
	 * @return List<ProductDTO>
	 * 							: Lista de productos completa
	 * @throws BusinessException 
	 * 							: Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "product", method = RequestMethod.GET)
	public @ResponseBody List<ProductDTO> listProducts() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.getProducts();
	}

	/**
	 * Metodo API que permite recuperar un Product especificando su ID
	 * 
	 * @param id
	 *            : Identificador de la entidad buscada.
	 * @return ProductDTO : producto buscado.
	 * @throws BusinessException
	 *             el producto estaba eliminado lógicamente
	 * @throws Exception
	 *             : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "product/{id}", method = RequestMethod.GET)
	public ProductDTO getProductById(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();
		return mCNQ.getProduct(id);
	}

	/**
	 * Metodo que permite eliminar un producto a partir de su identificador. Al
	 * eliminar el producto, sus los lotes asociados son eliminados físicamente.
	 * 
	 * @param id
	 *            identificador del producto a eliminar
	 * @throws BusinessException
	 *             errores al intentar realizar la operación
	 */
	@RequestMapping(value = "product/{id}", method = RequestMethod.DELETE)
	public void deleteProduct(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		mCNQ.deleteProduct(id);
	}
	
	/**
	 * Método que permite recuperar la lista completa de Presentaciones.
	 * 
	 * @return Lista de presentaciones.
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "presentation", method = RequestMethod.GET)
	public List<PresentationDTO> listPresentations() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<PresentationDTO> mPresentationDTOList = mCNQ.getPresentations();

		return mPresentationDTOList;
	}
	
	/**
	 * Método que permite recuperar la lista completa de Unidades de medida.
	 * 
	 * @return Lista de unidades de medida.
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "measure_unit", method = RequestMethod.GET)
	public List<MeasureUnitDTO> listMeasureUnits() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<MeasureUnitDTO> mMeasureUnitDTOList = mCNQ.getMeasureUnits();

		return mMeasureUnitDTOList;
	}

	@RequestMapping(value = "category", method = RequestMethod.POST)
	public long saveCategory(@RequestBody CategoryDTO pCategory) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.saveCategory(pCategory);

	}	

	@RequestMapping(value = "category", method = RequestMethod.GET)
	public List<CategoryDTO> listCategorys() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<CategoryDTO> mCategoryDTOList = mCNQ.getCategorys();

		return mCategoryDTOList;
	}
	
	@RequestMapping(value = "manufacturer", method = RequestMethod.POST)
	public long saveManufacturer(@RequestBody ManufacturerDTO pManufacturer) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.saveManufacturer(pManufacturer);

	}

	@RequestMapping(value = "manufacturer", method = RequestMethod.GET)
	public List<ManufacturerDTO> listManufacturers() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<ManufacturerDTO> mManufacturerDTOList = mCNQ.getManufacturers();

		return mManufacturerDTOList;
	}

	/**
	 * Método API que permite crear una <code>Presentation</code>
	 * 
	 * @param presentation
	 *            presentación a guardar
	 * @return identificador de la presentación guardada
	 * @throws BusinessException
	 *             errores de negocio producidos
	 */
	@RequestMapping(value = "presentation", method = RequestMethod.POST)
	public Long createPresentation(@RequestBody PresentationDTO presentation) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.createPresentation(presentation);

		return mId;
	}

	/**
	 * Método que permite recuperar la lista completa de drogas.
	 * 
	 * @return Lista de drogas.
	 */
	@RequestMapping(value = "drug", method = RequestMethod.GET)
	public List<DrugDTO> listDrugs() {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<DrugDTO> mDrugDTOList = mCNQ.getDrugs();

		return mDrugDTOList;
	}
	

	/**
	 * Método API que permite crear una <code>Drug</code>
	 * 
	 * @param drug
	 *            droga a guardar
	 * @return identificador de la droga guardada
	 * @throws BusinessException
	 *             errores de negocio producidos
	 */
	@RequestMapping(value = "drug", method = RequestMethod.POST)
	public Long createDrug(@RequestBody DrugDTO drug) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.createDrug(drug);

		return mId;
	}
	
	// FIN PRODUCTOS
	
	//=======================================================================================
	
	// PERSONAS
	/**
	 * Método que permite recuperar la lista completa de personas.
	 * 
	 * @return Lista de personas.
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "person", method = RequestMethod.GET)
	public List<Object> listPeople() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<Object> mPeopleList = mCNQ.getPeople();

		return mPeopleList;
	}
	
	/**
	 * Metodo que permite eliminar un Persona (tanto LegalPerson como NaturalPerson) 
	 * a partir de su identificador, es decir PersonId. 
	 * Al eliminar la Person, la misma se da de baja lógicamente, usando su atributo.
	 * 
	 * @param id
	 *            identificador de la Person (LegalPerson o NaturalPerson a eliminar)
	 * @throws BusinessException
	 *             errores al intentar realizar la operación
	 */
	@RequestMapping(value = "person/{id}", method = RequestMethod.DELETE)
	public void deletePerson(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		mCNQ.deletePerson(id);
	}
	
	/**
	 * Metodo API que permite crear un LegalPerson.
	 * 
	 * @param LegalPersonDTO: datos de la persona jurídica a crear
	 * @return Long : Identificador de la nueva persona legal en la BD.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "legalperson", method = RequestMethod.POST)
	public Long createLegalPerson(@RequestBody LegalPersonDTO pLegalPerson) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.saveLegalPerson(pLegalPerson);

		return mId;
	}
	
	/**
	 * Metodo API que permite recuperar la lista de los distintos Legal_Person.
	 * 
	 * @return List<LegalPersonDTO>
	 * 							: Lista de proveedores completa.
	 * @throws BusinessException 
	 * 							: Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "legalperson", method = RequestMethod.GET)
	public @ResponseBody List<LegalPersonDTO> listLegalPerson() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.getLegalPersons();
	}
	
	/**
	 * Metodo API que permite recuperar un LegalPerson especificando su ID
	 * 
	 * @param id
	 *            : Identificador de la entidad buscada.
	 * @return LegalPersonDTO : LegalPerson buscada.
	 * @throws BusinessException
	 *             el producto estaba eliminado lógicamente
	 * @throws Exception
	 *             : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "legalperson/{id}", method = RequestMethod.GET)
	public LegalPersonDTO getLegalPersonById(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();
		return mCNQ.getLegalPerson(id);
	}
	
	/**
	 * Metodo API que permite crear una persona física.
	 * 
	 * @param NaturalPersonDTO
	 *            : datos de la persona física a crear
	 * @return Long : Identificador de la nueva persona física en la BD.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "naturalperson", method = RequestMethod.POST)
	public Long createNaturalPerson(@RequestBody NaturalPersonDTO person) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.saveNaturalPerson(person);

		return mId;
	}
	
	/**
	 * Metodo API que permite recuperar una NaturalPerson especificando su ID
	 * 
	 * @param id
	 *            : Identificador de la entidad buscada.
	 * @return LegalPersonDTO : NaturalPerson buscada.
	 * @throws BusinessException
	 *             la persona estaba eliminado lógicamente
	 * @throws Exception
	 *             : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "naturalperson/{id}", method = RequestMethod.GET)
	public NaturalPersonDTO getNaturalPersonById(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();
		return mCNQ.getNaturalPerson(id);
	}
	
	@RequestMapping(value = "ivacategory", method = RequestMethod.POST)
	public long saveIVACategory(@RequestBody IVACategoryDTO pIVACategory) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.saveIVACategory(pIVACategory);

	}	

	@RequestMapping(value = "ivacategory", method = RequestMethod.GET)
	public List<IVACategoryDTO> listCategories() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		List<IVACategoryDTO> mIVACategoryDTOList = mCNQ.getIVACategories();

		return mIVACategoryDTOList;
	}
	
	// FIN PERSONAS
	
	//=======================================================================================
	
	// CLIENTES
	
	/**
	 * Método que permite recuperar la lista completa de personas que son clientes.
	 * 
	 * @return Lista de clientes.
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "client", method = RequestMethod.GET)
	public @ResponseBody List<PersonDTO> listClients() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.getClients();
	}
	
	// FIN CLIENTES
	
	//=======================================================================================
	
	// PROVEEDORES
	
	/**
	 * Metodo API que permite recuperar la lista de los distintos Legal_Person (Proveedores).
	 * 
	 * @return List<LegalPersonDTO>
	 * 							: Lista de proveedores completa.
	 * @throws BusinessException 
	 * 							: Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "supplier", method = RequestMethod.GET)
	public @ResponseBody List<LegalPersonDTO> listSupplier() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.getSuppliers();
	}
	
	// FIN PROVEEDORES
	
	//=======================================================================================
	
	// VENTAS
	
	/**
	 * Metodo API que permite crear una venta (SALE).
	 * 
	 * @param SaleDTO
	 *            : datos del saleo a crear
	 * @return Long : Identificador del nuevo saleo en la BD.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "sale", method = RequestMethod.POST)
	public Long createSale(@RequestBody SaleDTO sale) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		Long mId = mCNQ.createSale(sale);

		return mId;
	}

	/**
	 * Metodo API que permite recuperar la lista de las distintas 
	 * ventas (SALE).
	 * 
	 * @return List<SaleDTO>
	 * 							: Lista de ventas completa
	 * @throws BusinessException 
	 * 							: Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "sale", method = RequestMethod.GET)
	public @ResponseBody List<SaleDTO> listSales() throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();

		return mCNQ.getSales();
	}

	/**
	 * Metodo API que permite recuperar un Sale especificando su ID
	 * 
	 * @param id
	 *            : Identificador de la entidad buscada.
	 * @return SaleDTO : saleo buscado.
	 * @throws BusinessException
	 *             el saleo estaba eliminado lógicamente
	 * @throws Exception
	 *             : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "sale/{id}", method = RequestMethod.GET)
	public SaleDTO getSaleById(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();
		return mCNQ.getSale(id);
	}
	
	/**
	 * Metodo API que permite recuperar un cliente especificando su ID
	 * 
	 * @param id
	 *            : Identificador de la entidad buscada.
	 * @return PersonDTO : el cliente buscado.
	 * @throws BusinessException
	 *             no existe un cliente con ese ID
	 */
	@RequestMapping(value = "client/{id}", method = RequestMethod.GET)
	public PersonDTO getClientById(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();
		
		PersonDTO mPerson = null;
		
		mPerson = mCNQ.getNaturalPerson(id);
		
		if(mPerson == null){
			LegalPersonDTO mLegalPerson = mCNQ.getLegalPerson(id);
			
			if(mLegalPerson!= null && mLegalPerson.isClient()){
				mPerson = mLegalPerson;
			}else{
				throw new BusinessException("No existe un cliente con ese identificador.");
			}
		}
		
		return mPerson;
	}
	
	/**
	 * Metodo API que permite dar de baja un cliente especificando su ID
	 * 
	 * @param id
	 *            : Identificador de la entidad buscada.
	 * @return mId : el identificador del cliente dado de baja.
	 * @throws BusinessException
	 *             no existe un cliente con ese ID
	 */
	@RequestMapping(value = "client/{id}", method = RequestMethod.DELETE)
	public void deleteClientById(@PathVariable Long id) throws BusinessException {
		CommandAndQueries mCNQ = new CommandAndQueries();
		mCNQ.deletePerson(id);
	}
	
	// FIN VENTAS
	
	//=======================================================================================
	
	// IMPRESIÓN
	
	/***
	 * Este método permite ejecutar una impresión de testing.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "printtest", method = RequestMethod.POST)
	public void printTest() throws BusinessException {
		
		ZebraPrintHelper.PrintTest();
	}
	
	/***
	 * Este método permite ejecutar una impresión de testing.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "printbarcode", method = RequestMethod.POST)
	public void printBarCode(@RequestBody PrintItemDTO dto) throws BusinessException {
		
		ZebraPrintHelper.PrintCode(dto.getCode());
	}
	
	/***
	 * Este método permite ejecutar una impresión de testing.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "printbatch", method = RequestMethod.POST)
	public void printBatch(@RequestBody BatchDTO dto) throws BusinessException {
		
		ZebraPrintHelper.PrintBatch(dto);
	}
	
	// FIN IMPRESIÓN
	
	//=======================================================================================
	
	
	/**
	 * Método maneja las excepciones que se producen en el controlador.
	 * 
	 * Solamente maneja las excepciones cuyo tipo son: BusinessException
	 * 
	 * Los parámetros de entrada a este metodo llegan de forma automática.
	 * 
	 * @param mRequest
	 *            : Este parámetro contiene la información del request generado
	 *            desde el cliente. Ejemplo: request.getRequestURL() devuelve la
	 *            URL del servicio consumido. Ej.: "www.genesis.com/product/1"
	 * @param mBusinessException
	 *            : Este parámetro contiene la excepción que se generó durante
	 *            la ejecución de un método en el controlador. Tiene información
	 *            sobre la clase, método y detalles de la excepción.
	 * @return ResponseEntity<ErrorDTO> El ResponseEntity contiene la
	 *         información del código HTTP retornado al cliente (EJ: 404, 500,
	 *         etc.) También contiene la información de la clase T que envuelve,
	 *         en este caso ErrorDTO. En conclusion, el cliente recibe
	 *         no solamente el codigo de error, sino tambien detalles gracias a
	 *         la entidad envuelta.
	 */
	@ExceptionHandler(BusinessException.class)
	public @ResponseBody ResponseEntity<ErrorDTO> handleBusinessException(HttpServletRequest mRequest,
			BusinessException mBusinessException) {

		// Creamos el objeto DTO que sera el que viaje al cliente.
		ErrorDTO mErrorDTO = new ErrorDTO();
		mErrorDTO.setMessage(mBusinessException.getMessage());
		mErrorDTO.setStackTrace(mBusinessException.getStackTraceString());

		// creación de la respuesta
		ResponseEntity<ErrorDTO> mResponse = new ResponseEntity<ErrorDTO>(mErrorDTO, HttpStatus.CONFLICT);

		return mResponse;
	}

}
