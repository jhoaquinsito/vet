package backend.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import backend.form_of_sale.FormOfSaleDTO;
import backend.form_of_sale.FormOfSaleForSalesReportDTO;
import backend.person.PersonDTO;
import backend.person.PersonForDropdownDTO;
import backend.person.children.natural_person.NaturalPersonDTO;
import backend.person.iva_category.IVACategoryDTO;
import backend.person.settlement.SettlementDTO;
import backend.person.children.legal_person.LegalPersonDTO;
import backend.product.ProductDTO;
import backend.product.ProductForSaleDTO;
import backend.product.batch.BatchDTO;
import backend.product.batch.BatchPrintDTO;
import backend.product.category.CategoryDTO;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.PresentationDTO;
import backend.report.person_balance.PersonBalanceDTO;
import backend.sale.SaleDTO;
import backend.sale.SaleForReportDTO;
import backend.sale.SaleLiteDTO;
import backend.utils.DateHelper;
import backend.utils.ZebraPrintHelper;

/**
 * Este <code>Controlador</code> es el encargado de recibir los request desde la
 * interfaz, procesarlos, y devolver la respuesta al cliente.
 * 
 * @author genesis
 *
 */
@RestController
@CrossOrigin
@RequestMapping("api/")
public class ApplicationRESTController {
	
	@Autowired CommandAndQueries iCommandAndQueries;
	@Autowired ReportQueries iReportQueries;

	//=======================================================================================
	
	// PRODUCTOS
	/**
	 * Método que permite guardar un conjunto de productos.
	 * 
	 * @param pBunchOfProducts conjunto de productos a guardar
	 * @throws BusinessException errores de negocio
	 */
	@RequestMapping(value = "product/bunch", method = RequestMethod.POST)
	public void saveBunchOfProducts(@RequestBody List<ProductDTO> pBunchOfProducts) throws BusinessException {
		this.iCommandAndQueries.saveBunchOfProducts(pBunchOfProducts);
	}
	
	/**
	 * Metodo API que permite crear un Producto.
	 * 
	 * @param ProductDTO: datos del producto a crear
	 * @return Long : Identificador del nuevo producto en la BD.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "product", method = RequestMethod.POST)
	public Long createProduct(@RequestBody ProductDTO product) throws BusinessException {

		Long mId = this.iCommandAndQueries.saveProduct(product);

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

		return this.iCommandAndQueries.getProducts();
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
		return this.iCommandAndQueries.getProduct(id);
	}
	
	/**
	 * Metodo API que permite recuperar un Producto a través de un código de lote
	 * 
	 * @param batchCode el código de lote
	 * @throws BusinessException
	 */
	@RequestMapping(value = "product/bybatchcode/{batchCode}", method = RequestMethod.GET)
	public ProductForSaleDTO getProductByBatchCode(@PathVariable String batchCode) throws BusinessException {
		return this.iCommandAndQueries.getProductForSaleByBatchCode(batchCode);
	}
	
	/**
	 * Metodo API que permite recuperar una lista de Product 
	 * especificando su Name
	 * 
	 * @param name
	 *            : Nombre de la entidad buscada.
	 * @return List<ProductDTO> : productos con "name" similar.
	 * @throws BusinessException
	 *             : Excepcion de negocio,.
	 * @throws Exception
	 *             : Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "products/byname/{name}", method = RequestMethod.GET)
	public List<ProductDTO> getProductsByName(@PathVariable String name) throws BusinessException {
		return this.iCommandAndQueries.getProductsByName(name);
	}

	/**
	 * Método API que permite recuperar una lista de Product
	 * filtrando aquellos productos cuyos lotes cumplan
	 * ciertas condiciones de vencimiento
	 * @param days
	 * @param beginDate : RequestParam, opcional, que permite especificar una fecha de inicio en formato ISO
	 * @return
	 * @throws BusinessException
	 * 				: Excepcion de negocio, manejada por: handleBusinessException
	 * 
	 */
	@RequestMapping(value = "products/byduedate", method = RequestMethod.GET)
	public List<BatchDTO> getProductsByExpirationDate(@RequestParam(value = "days", required = true) Integer days, 
													  @RequestParam(value = "beginDate", required = false) Integer beginDate) throws BusinessException {
		Date mBeginDate = new Date();
		

		if(beginDate != null)
			mBeginDate = DateHelper.getDate(beginDate);
		
		return this.iCommandAndQueries.getBatchesByDueDateAndDays(mBeginDate, days);
	}
	
	@RequestMapping(value = "products/toExpirate", method = RequestMethod.GET)
	public boolean areProductsToExpirate(@RequestParam(value = "days", required = true) Integer days, 
										 @RequestParam(value = "beginDate", required = false) Integer beginDate) throws BusinessException {
		Date mBeginDate = new Date();
		

		if(beginDate != null)
			mBeginDate = DateHelper.getDate(beginDate);
		
		return (this.iCommandAndQueries.getBatchesByDueDateAndDays(mBeginDate, days)).size() > 0;
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

		this.iCommandAndQueries.deleteProduct(id);
	}
	
	/**
	 * Método que permite recuperar la lista completa de Presentaciones.
	 * 
	 * @return Lista de presentaciones.
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "presentation", method = RequestMethod.GET)
	public List<PresentationDTO> listPresentations() throws BusinessException {

		List<PresentationDTO> mPresentationDTOList = this.iCommandAndQueries.getPresentations();

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

		List<MeasureUnitDTO> mMeasureUnitDTOList = this.iCommandAndQueries.getMeasureUnits();

		return mMeasureUnitDTOList;
	}

	@RequestMapping(value = "category", method = RequestMethod.POST)
	public long saveCategory(@RequestBody CategoryDTO pCategory) throws BusinessException {

		return this.iCommandAndQueries.saveCategory(pCategory);

	}	

	@RequestMapping(value = "category", method = RequestMethod.GET)
	public List<CategoryDTO> listCategorys() throws BusinessException {
		

		List<CategoryDTO> mCategoryDTOList = iCommandAndQueries.getCategorys();

		return mCategoryDTOList;
	}
	
	@RequestMapping(value = "manufacturer", method = RequestMethod.POST)
	public long saveManufacturer(@RequestBody ManufacturerDTO pManufacturer) throws BusinessException {

		return this.iCommandAndQueries.saveManufacturer(pManufacturer);

	}

	@RequestMapping(value = "manufacturer", method = RequestMethod.GET)
	public List<ManufacturerDTO> listManufacturers() throws BusinessException {

		List<ManufacturerDTO> mManufacturerDTOList = this.iCommandAndQueries.getManufacturers();

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

		Long mId = this.iCommandAndQueries.createPresentation(presentation);

		return mId;
	}

	/**
	 * Método que permite recuperar la lista completa de drogas.
	 * 
	 * @return Lista de drogas.
	 */
	@RequestMapping(value = "drug", method = RequestMethod.GET)
	public List<DrugDTO> listDrugs() {

		List<DrugDTO> mDrugDTOList = this.iCommandAndQueries.getDrugs();

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

		Long mId = this.iCommandAndQueries.createDrug(drug);

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

		List<Object> mPeopleList = this.iCommandAndQueries.getPeople();

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

		this.iCommandAndQueries.deletePerson(id);
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

		Long mId = this.iCommandAndQueries.saveLegalPerson(pLegalPerson);

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

		return this.iCommandAndQueries.getLegalPersons();
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
		return this.iCommandAndQueries.getLegalPerson(id);
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

		Long mId = this.iCommandAndQueries.saveNaturalPerson(person);

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
		return this.iCommandAndQueries.getNaturalPerson(id);
	}
	
	@RequestMapping(value = "ivacategory", method = RequestMethod.POST)
	public long saveIVACategory(@RequestBody IVACategoryDTO pIVACategory) throws BusinessException {

		return this.iCommandAndQueries.saveIVACategory(pIVACategory);

	}	

	@RequestMapping(value = "ivacategory", method = RequestMethod.GET)
	public List<IVACategoryDTO> listCategories() throws BusinessException {

		List<IVACategoryDTO> mIVACategoryDTOList = this.iCommandAndQueries.getIVACategories();

		return mIVACategoryDTOList;
	}
	
	@RequestMapping(value = "report/person-balance", method = RequestMethod.GET)
	public List<PersonBalanceDTO> getAllPersonBalances(){
		return this.iReportQueries.getAllPersonBalances();
	}

	@RequestMapping(value = "report/products-without-stock", method = RequestMethod.GET)
	public @ResponseBody List<ProductDTO> getProductsWithoutMinimumStock() throws BusinessException {
		return this.iReportQueries.getProductsWithoutMinimumStock();
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

		return this.iCommandAndQueries.getClients();
	}
	
	/**
	 * Método que permite recuperar la lista completa de personas que son clientes para ser utilizada en un dropdown.
	 * Esta lista no contiene toda la información de los clientes, sino sólo la necesaria para
	 * un dropdown: Id, Name, Settlements.
	 * 
	 * @return Lista de clientes.
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "client/dropdown", method = RequestMethod.GET)
	public @ResponseBody List<PersonForDropdownDTO> listClientsForDropdown() throws BusinessException {

		return this.iCommandAndQueries.getClientsForDropdown();
	}
	
	@RequestMapping(value = "client/{pClientId}/settlements", method = RequestMethod.POST)
	public void setClientSettlements(@PathVariable Long pClientId, @RequestBody List<SettlementDTO> pUpdatedClientSettlements) throws BusinessException {

		this.iCommandAndQueries.setUpdatedSettlementsTo(pClientId, pUpdatedClientSettlements);

	}
	
	@RequestMapping(value = "client/{pClientId}/settlements", method = RequestMethod.GET)
	public @ResponseBody List<SettlementDTO> getClientSettlements(@PathVariable Long pClientId) throws BusinessException {

		return this.iCommandAndQueries.getClientSettlements(pClientId);
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

		return this.iCommandAndQueries.getSuppliers();
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
	public Long createSale(@RequestBody SaleLiteDTO sale) throws BusinessException {

		Long mId = this.iCommandAndQueries.createSale(sale);

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

		return this.iCommandAndQueries.getSales();
	}
	
	/**
	 * Método API que permite recuperar la lista de VENTAS para el reporte de ventas.
	 * Recupera las ventas que se encuentran comprendida en las fechas y 
	 * bajo el parámetro del tipo de pago especificado
	 * @param payForm :
	 * 					Forma de pago realizado, opciones: Todas, Efectivo o Cuenta Corriente
	 * @param beginDate:
	 * 					Fecha de inicio a considerar
	 * @param endDate:
	 * 					Fecha de fin a considerar
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "sale/report", method = RequestMethod.POST)
	public List<SaleLiteDTO> getSalesForReport
	(@RequestBody FormOfSaleForSalesReportDTO pFormOfSaleForSalesReport) throws BusinessException {
		//TODO esta logica no debería estar acá, analizar responsabilidades:
		Date mBeginDate = new Date();
		Date mEndDate = new Date();

		if(pFormOfSaleForSalesReport.getBeginDate() != null)
			mBeginDate 	= DateHelper.getDate(pFormOfSaleForSalesReport.getBeginDate());
		if(pFormOfSaleForSalesReport.getEndDate() != null)
			mEndDate 	= DateHelper.getDate(pFormOfSaleForSalesReport.getEndDate());
		
		//Como la IsoDueDate carece de la parte temporal, es necesario agregar 1 día mas, para contemplar el dia entero en la EndDate
		Calendar c = Calendar.getInstance();
		c.setTime(mEndDate);
		c.add(Calendar.DATE, 1);  
		mEndDate = (c.getTime());  
		
		
		return this.iCommandAndQueries.getSalesForReport(mBeginDate, mEndDate, pFormOfSaleForSalesReport);
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
		return this.iCommandAndQueries.getSale(id);
	}
	
	
	/**
	 * Metodo API que permite recuperar las ventas de un cliente que no han sido pagadas.
	 * 
	 * @return List<SaleDTO>
	 * 							: Lista de ventas sin pagar del cliente
	 * @throws BusinessException 
	 * 							: Excepcion de negocio, manejada por: handleBusinessException
	 */
	@RequestMapping(value = "duesales/{pClientId}", method = RequestMethod.GET)
	public @ResponseBody List<SaleForReportDTO> getDueSalesByClientId(@PathVariable Long pClientId) throws BusinessException {

		return this.iCommandAndQueries.getDueSalesByClientId(pClientId);
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
		
		PersonDTO mPerson = null;
		
		mPerson = this.iCommandAndQueries.getNaturalPerson(id);
		
		if(mPerson == null){
			LegalPersonDTO mLegalPerson = this.iCommandAndQueries.getLegalPerson(id);
			
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
		this.iCommandAndQueries.deletePerson(id);
	}
	
	@RequestMapping(value = "formofsale", method = RequestMethod.GET)
	public List<FormOfSaleDTO> listFormOfSales() throws BusinessException {

		List<FormOfSaleDTO> mFormOfSaleDTOList = this.iCommandAndQueries.getFormOfSales();

		return mFormOfSaleDTOList;
	}
	
	// FIN VENTAS
	
	//=======================================================================================
	
	// IMPRESIÓN
	
	/***
	 * Este método permite ejecutar una impresión de un SALE.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "printsale/{id}", method = RequestMethod.POST)
	public void printSale(@PathVariable Long id) throws BusinessException {
		
		 
		SaleDTO sale = this.iCommandAndQueries.getSale(id);
		ZebraPrintHelper.PrintSale(sale);
	}
	
	
	/***
	 * Este método permite ejecutar una impresión de un BATCH.
	 * @throws BusinessException
	 */
	@RequestMapping(value = "printbatch", method = RequestMethod.POST)
	public void printBatch(@RequestBody BatchPrintDTO dto) throws BusinessException {
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
