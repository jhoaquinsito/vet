package backend.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import backend.exception.BusinessException;
import backend.person.Person;
import backend.person.PersonDTO;
import backend.person.PersonService;
import backend.person.children.legal_person.LegalPerson;
import backend.person.children.legal_person.LegalPersonDTO;
import backend.person.children.legal_person.LegalPersonService;
import backend.person.children.natural_person.NaturalPerson;
import backend.person.children.natural_person.NaturalPersonDTO;
import backend.person.children.natural_person.NaturalPersonService;
import backend.person.iva_category.IVACategory;
import backend.person.iva_category.IVACategoryDTO;
import backend.person.iva_category.IVACategoryService;
import backend.product.BatchCodeGenerator;
import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductService;
import backend.product.category.Category;
import backend.product.category.CategoryDTO;
import backend.product.category.CategoryService;
import backend.product.drug.Drug;
import backend.product.drug.DrugDTO;
import backend.product.drug.DrugService;
import backend.product.manufacturer.Manufacturer;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.manufacturer.ManufacturerService;
import backend.product.measure_unit.MeasureUnit;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.measure_unit.MeasureUnitService;
import backend.product.presentation.Presentation;
import backend.product.presentation.PresentationDTO;
import backend.product.presentation.PresentationService;
import backend.sale.Sale;
import backend.sale.SaleCons;
import backend.sale.SaleDTO;
import backend.sale.SaleLiteDTO;
import backend.sale.SaleService;
import backend.saleline.SaleLine;
import backend.saleline.SaleLineLiteDTO;
import backend.utils.OrikaMapperFactory;
import ma.glasnost.orika.MapperFacade;

/**
 * <code>CommandAndQueries</code> representa el conjunto de comandos y consultas posibles de realizar al dominio.
 * Los comandos y consultas son utilizados a nivel de presentación (RestController) y esta clase se encarga
 * de convertir los DTO recibidos en objetos de dominio, implementar el comando o la consulta deseada utilizando
 * objetos del dominio y luego convertir la respuesta a DTO para responder a la capa de presentación (si
 * es necesario).
 * 
 * El conjunto de comandos y consultas tiene asociado:
 * un mappeador de objetos (DTO vs dominio, o viceversa): <strong>Mapper</strong>.
 * 
 * @author tomas
 *
 */
public class CommandAndQueries {
	// MapperFacade de Orika (librería para mapping)
	private MapperFacade iMapper; 
	private static final String cPRODUCT_NULL_EXCEPTION_MESSAGE = "El producto no tiene valores.";
	private static final String cNATURAL_PERSON_NULL_EXCEPTION_MESSAGE = "La persona física no tiene valores válidos.";
	
	/**
	 * Constructor.
	 */
	public CommandAndQueries() {
		super();
		// obtengo un mapper facade de la factory Orika
		this.iMapper = OrikaMapperFactory.getMapperFacade();
	}
	
	//=======================================================================================
	
	// PRODUCTOS
	
	/**
	 * Este método es un comando que permite guardar un producto.
	 * 
	 * @param pProductDTO producto a guardar
	 * @return identificador del producto guardado
	 * @throws BusinessException 
	 */
	// TODO cambiar nombre de este método por "createProduct"
	public Long saveProduct(ProductDTO pProductDTO) throws BusinessException {
		
		ProductService mProductService = new ProductService();
		
		// map dto to domain object
		Product mProduct;
		if (pProductDTO != null){
			mProduct = iMapper.map(pProductDTO, Product.class);
		} else {
			throw new BusinessException(CommandAndQueries.cPRODUCT_NULL_EXCEPTION_MESSAGE);
		}
		
		
		mProduct = mProductService.save(mProduct);
		
		return mProduct.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Productos
	 * @return lista de Producto
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<ProductDTO> getProducts() throws BusinessException{
		ProductService mProductService = new ProductService();
		
		Iterable<Product> mProduct = mProductService.getAll();
		
		List<ProductDTO> mProductDTOList = new ArrayList<ProductDTO>();
		
		for (Product bProduct : mProduct){
			mProductDTOList.add(this.iMapper.map(bProduct,ProductDTO.class));
		}
		
		return mProductDTOList;
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Productos
	 * filtrados a traves de su:
	 * @param pProductName : Fragmento de nombre de un producto
	 * @return
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<ProductDTO> getProductsByName(String pProductName) throws BusinessException{
		ProductService mProductService = new ProductService();
		
		Iterable<Product> mProduct = mProductService.getProductListByName(pProductName);
		
		List<ProductDTO> mProductDTOList = new ArrayList<ProductDTO>();
		
		for (Product bProduct : mProduct){
			mProductDTOList.add(this.iMapper.map(bProduct,ProductDTO.class));
		}
		
		return mProductDTOList;
	}
	

	/**
	 * Este método es una consulta que devuelve la lista completa de Productos
	 * filtrados a traves de su:
	 * @param pBatchCode : Fragmento de codigo de barra de un lote.
	 * @return
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<ProductDTO> getProductsByBatchCode(String pBatchCode) throws BusinessException{
		ProductService mProductService = new ProductService();
		
		Iterable<Product> mProduct = mProductService.getProductListByBatchCode(pBatchCode);
		
		List<ProductDTO> mProductDTOList = new ArrayList<ProductDTO>();
		
		for (Product bProduct : mProduct){
			mProductDTOList.add(this.iMapper.map(bProduct,ProductDTO.class));
		}
		
		return mProductDTOList;
	}
	
	/**
	 * Este método es una consulta que obtiene un producto a partir de
	 * su identificador.
	 * @param pProductId identificador del producto
	 * @return producto encontrado
	 * @throws BusinessException el producto estaba eliminado lógicamente
	 */
	public ProductDTO getProduct(Long pProductId) throws BusinessException{
		ProductService mProductService = new ProductService();
		
		Product mProduct = mProductService.get(pProductId);
		
		ProductDTO mProductDTO = this.iMapper.map(mProduct, ProductDTO.class);
		
		return mProductDTO;
	}
	
	/**
	 * Este método es una consulta que obtiene un producto a partir de
	 * un código de lote.
	 * @param pProductBatchCode un código de lote
	 * @return mProductDTO producto asociado al código de lote
	 * @throws BusinessException
	 */
	public ProductDTO getProductByBatchCode(String pProductBatchCode) throws BusinessException{
		ProductService mProductService = new ProductService();
		
		Long mProductId = BatchCodeGenerator.getProductId(pProductBatchCode);
		
		Product mProduct = mProductService.get(mProductId);
		
		ProductDTO mProductDTO = this.iMapper.map(mProduct, ProductDTO.class);
		
		return mProductDTO;
	}
	
	/**
	 * Este método es un comando que elimina un producto a partir de su identificador.
	 * Al eliminar el producto, sus los lotes asociados son eliminados físicamente.
	 * @param pProductId identificador del producto a eliminar
	 * @throws BusinessException errores de negocio al intentar hacer la operación
	 */
	public void deleteProduct(Long pProductId) throws BusinessException{
		ProductService mProductService = new ProductService();
		
		// elimino el producto
		mProductService.delete(pProductId);
		
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Presentaciones.
	 * @return lista de Presentaciones
	 * @throws BusinessException
	 */
	public List<PresentationDTO> getPresentations() throws BusinessException{
		PresentationService mPresentationService = new PresentationService();
		
		Iterable<Presentation> mPresentations = mPresentationService.getAll();
		
		List<PresentationDTO> mPresentationDTOList = new ArrayList<PresentationDTO>();
		
		for (Presentation bPresentation : mPresentations){
			mPresentationDTOList.add(this.iMapper.map(bPresentation,PresentationDTO.class));
		}
		
		return mPresentationDTOList;
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de unidades de medida.
	 * @return lista de unidades de medida
	 * @throws BusinessException 
	 */
	public List<MeasureUnitDTO> getMeasureUnits() throws BusinessException{
		MeasureUnitService mMeasureUnitService = new MeasureUnitService();
		
		Iterable<MeasureUnit> mMeasureUnits = mMeasureUnitService.getAll();
		
		List<MeasureUnitDTO> mMeasureUnitDTOList = new ArrayList<MeasureUnitDTO>();
		
		for (MeasureUnit bMeasureUnit : mMeasureUnits){
			mMeasureUnitDTOList.add(this.iMapper.map(bMeasureUnit,MeasureUnitDTO.class));
		}
		
		return mMeasureUnitDTOList;
	}	
	

	/**
	 * Este método es una consulta que devuelve la lista completa de Categorías
	 * @return lista de Categorias
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<CategoryDTO> getCategorys() throws BusinessException{
		CategoryService mCategoryService = new CategoryService();
		
		Iterable<Category> mCategory = mCategoryService.getAll();
		
		List<CategoryDTO> mCategoryDTOList = new ArrayList<CategoryDTO>();
		
		for (Category bCategory : mCategory){
			mCategoryDTOList.add(this.iMapper.map(bCategory,CategoryDTO.class));
		}
		
		return mCategoryDTOList;
	}
	
	/**
	 * Este método es un comando que permite guardar una Categoría
	 * @param pCategory : Categoría a guardar
	 * @return identificador de la categoria guardado
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public long saveCategory(CategoryDTO pCategory) throws BusinessException {
		CategoryService mCategoryService = new CategoryService();
		
		Category bCategory = new Category();
		
		bCategory = this.iMapper.map(pCategory,Category.class);
		
		return this.iMapper.map(mCategoryService.save(bCategory),CategoryDTO.class).getId();
		
	}

	/**
	 * Este método es una consulta que devuelve la lista completa de laboratorios
	 * @return lista de laboratorios
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public List<ManufacturerDTO> getManufacturers() throws BusinessException{
		ManufacturerService mManufacturerService = new ManufacturerService();
		
		Iterable<Manufacturer> mManufacturer = mManufacturerService.getAll();
		
		List<ManufacturerDTO> mManufacturerDTOList = new ArrayList<ManufacturerDTO>();
		
		for (Manufacturer bManufacturer : mManufacturer){
			mManufacturerDTOList.add(this.iMapper.map(bManufacturer,ManufacturerDTO.class));
		}
		
		return mManufacturerDTOList;
	}

	/**
	 * Este método es un comando que permite guardar un Laboratorio
	 * @param pManufacturer : Laboratorio a guardar
	 * @return identificador del laboratorio guardado
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public long saveManufacturer(ManufacturerDTO pManufacturer) throws BusinessException {
		ManufacturerService mManufacturerService = new ManufacturerService();
		
		Manufacturer bManufacturer = new Manufacturer();
		
		bManufacturer = this.iMapper.map(pManufacturer,Manufacturer.class);
		
		return this.iMapper.map(mManufacturerService.save(bManufacturer),ManufacturerDTO.class).getId();
		
	}

	/**
	 * Este método es un comando que permite guardar una presentación.
	 * @param pPresentationDTO presentación a guardar
	 * @return identificador de la presentación guardada
	 * @throws BusinessException errores de negocio producidos
	 */
	public Long createPresentation(PresentationDTO pPresentationDTO) throws BusinessException{
		PresentationService mPresentationService = new PresentationService();
		
		// map dto to domain object
		Presentation mPresentation = iMapper.map(pPresentationDTO, Presentation.class);
		
		mPresentation = mPresentationService.create(mPresentation);
		
		return mPresentation.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de drogas
	 * @return lista de drogas
	 */
	public List<DrugDTO> getDrugs(){
		DrugService mDrugService = new DrugService();
		
		Iterable<Drug> mDrugs = mDrugService.getAll();
		
		List<DrugDTO> mDrugDTOList = new ArrayList<DrugDTO>();
		
		for (Drug bDrug : mDrugs){
			mDrugDTOList.add(this.iMapper.map(bDrug,DrugDTO.class));
		}
		
		return mDrugDTOList;
	}
	
	/**
	 * Este método es un comando que permite guardar una droga.
	 * @param pDrugDTO presentación a guardar
	 * @return identificador de la presentación guardada
	 * @throws BusinessException errores de negocio producidos
	 */
	public Long createDrug(DrugDTO pDrugDTO) throws BusinessException{
		DrugService mDrugService = new DrugService();
		
		// map dto to domain object
		Drug mDrug = iMapper.map(pDrugDTO, Drug.class);
		
		mDrug = mDrugService.create(mDrug);
		
		return mDrug.getId();
	}
	
	// FIN PRODUCTOS
	
	//=======================================================================================
	
	// PERSONAS
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Personas
	 * @return lista de Personas Físicas
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<Object> getPeople() throws BusinessException{
		LegalPersonService mLegalPersonService = new LegalPersonService();
		NaturalPersonService mNaturalPersonService = new NaturalPersonService();
		
		Iterable<LegalPerson> mLegalPersonList = mLegalPersonService.getAll();
		
		Iterable<NaturalPerson> mNaturalPersonList = mNaturalPersonService.getAll();
		
		List<Object> mPersonList = new ArrayList<Object>();
		
		for (LegalPerson bLegalPerson : mLegalPersonList){
			mPersonList.add(this.iMapper.map(bLegalPerson,LegalPersonDTO.class));
		}
		
		for (NaturalPerson bNaturalPerson : mNaturalPersonList){
			mPersonList.add(this.iMapper.map(bNaturalPerson,NaturalPersonDTO.class));
		}
		
		return mPersonList;
	}
	
	//TODO - Agregar comentario
	public PersonDTO getPerson(Long pPersonId)throws BusinessException {
		PersonService mPersonService = new PersonService();
		
		Person mPerson = mPersonService.get(pPersonId);
		
		return this.iMapper.map(mPerson,PersonDTO.class);
	}
	
	/**
	 * Este método es un comando que elimina una Persona a partir de su identificador.
	 * La eliminación es de tipo Lógica.
	 * @param pPersonId identificador de la persona asociada.
	 * @throws BusinessException errores de negocio al intentar hacer la operación
	 */
	public void deletePerson(Long pPersonId) throws BusinessException {
		PersonService mPersonService = new PersonService();
		
		// elimino la persona
		mPersonService.delete(pPersonId);
	}
	
	/**
	 * Este método es un comando que permite guardar un LegalPerson
	 * @param pLegalPersonDTO : Persona legal a guardar.
	 * @return identificador de la Persona que es una Persona Legal.
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public Long saveLegalPerson(LegalPersonDTO pLegalPersonDTO) throws BusinessException {
		LegalPersonService mLegalPersonService = new LegalPersonService();
		
		// map dto to domain object
		LegalPerson mLegalPerson = iMapper.map(pLegalPersonDTO, LegalPerson.class);
		
		//Si el cliente tiene ventas adeudadas
		if(!this.clientIsInDebt(mLegalPerson)){
			//Cancelo la deuda del cliente
			this.cancelClientDebt(mLegalPerson);
			//Descuento los pagos del cliente
			mLegalPerson.discountSettlements();
		}
		
		mLegalPerson = mLegalPersonService.save(mLegalPerson);
		
		return mLegalPerson.getId();
	}
	
	/***
	 * Este método es una consulta que devuelve la lista completa de Personas Legales
	 * cuya única condición es que estén activos.
	 * está activo.
	 * @return lista de Persona Legal.
	 * @throws BusinessException
	 */
	public List<LegalPersonDTO> getLegalPersons() throws BusinessException {
		LegalPersonService mLegalPersonService = new LegalPersonService();
		
		Iterable<LegalPerson> mLegalPerson = mLegalPersonService.getAll();
		
		List<LegalPersonDTO> mLegalPersonDTOList = new ArrayList<LegalPersonDTO>();
		
		for (LegalPerson bLegalPerson : mLegalPerson){
			mLegalPersonDTOList.add(this.iMapper.map(bLegalPerson,LegalPersonDTO.class));
		}
		
		return mLegalPersonDTOList;
	}
	
	/**
	 * Este método es una consulta que obtiene una persona legal a partir de
	 * su identificador.
	 * @param pLegalPersonId: identificador de la persona legal.
	 * @return persona legal encontrada
	 * @throws BusinessException
	 */
	public LegalPersonDTO getLegalPerson(Long pLegalPersonId) throws BusinessException {
		LegalPersonService mLegalPersonService = new LegalPersonService();
		
		LegalPerson mLegalPerson = mLegalPersonService.get(pLegalPersonId);
		
		LegalPersonDTO mLegalPersonDTO = this.iMapper.map(mLegalPerson, LegalPersonDTO.class);
		
		return mLegalPersonDTO;
	}
	
	/**
	 * Este método es un comando que permite guardar un producto.
	 * 
	 * @param pProductDTO producto a guardar
	 * @return identificador del producto guardado
	 * @throws BusinessException 
	 */
	public Long saveNaturalPerson(NaturalPersonDTO pNaturalPersonDTO) throws BusinessException {
		
		NaturalPersonService mNaturalPersonService = new NaturalPersonService();
		
		// map dto to domain object
		NaturalPerson mNaturalPerson;
		if (pNaturalPersonDTO != null){
			mNaturalPerson = iMapper.map(pNaturalPersonDTO, NaturalPerson.class);
		} else {
			throw new BusinessException(CommandAndQueries.cNATURAL_PERSON_NULL_EXCEPTION_MESSAGE);
		}
		
		//Si el cliente tiene ventas adeudadas
		if(!this.clientIsInDebt(mNaturalPerson)){
			//Cancelo la deuda del cliente
			this.cancelClientDebt(mNaturalPerson);
			//Descuento los pagos del cliente
			mNaturalPerson.discountSettlements();
		}
		
		mNaturalPerson = mNaturalPersonService.save(mNaturalPerson);
		
		return mNaturalPerson.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Personas Físicas
	 * @return lista de Personas Físicas
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<Object> getNaturalPeople() throws BusinessException{
		NaturalPersonService mNaturalPersonService = new NaturalPersonService();
		
		Iterable<NaturalPerson> mNaturalPersonList = mNaturalPersonService.getAll();
		
		List<Object> mNaturalPersonListRes = new ArrayList<Object>();
		
		for (NaturalPerson bNaturalPerson : mNaturalPersonList){
			if(bNaturalPerson.isActive())
			mNaturalPersonListRes.add(this.iMapper.map(bNaturalPerson,NaturalPersonDTO.class));
		}
		
		return mNaturalPersonListRes;
	}
	
	/**
	 * Este método es una consulta que obtiene una persona física a partir de
	 * su identificador.
	 * @param pPersonId: identificador de la persona.
	 * @return persona física encontrada
	 * @throws BusinessException
	 */
	public NaturalPersonDTO getNaturalPerson(Long pPersonId) throws BusinessException {
		NaturalPersonService mNaturalPersonService = new NaturalPersonService();
		
		NaturalPerson mNaturalPerson = mNaturalPersonService.get(pPersonId);
		
		NaturalPersonDTO mNaturalPersonDTO = this.iMapper.map(mNaturalPerson, NaturalPersonDTO.class);
		
		return mNaturalPersonDTO;
	}
	
	/**
	 * Este método es un comando que permite guardar una Categoría de IVA
	 * @param pIVACategory : Categoría a guardar
	 * @return identificador de la categoria guardado
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public long saveIVACategory(IVACategoryDTO pIVACategory) throws BusinessException {
		IVACategoryService mIVACategoryService = new IVACategoryService();
		
		IVACategory mIVACategory = new IVACategory();
		
		mIVACategory = this.iMapper.map(pIVACategory,IVACategory.class);
		
		return this.iMapper.map(mIVACategoryService.save(mIVACategory),IVACategoryDTO.class).getId();
		
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Categorías de IVA
	 * @return lista de Categorias de IVA
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<IVACategoryDTO> getIVACategories() throws BusinessException{
		IVACategoryService mIVACategoryService = new IVACategoryService();
		
		Iterable<IVACategory> mIVACategory = mIVACategoryService.getAll();
		
		List<IVACategoryDTO> mIVACategoryDTOList = new ArrayList<IVACategoryDTO>();
		
		for (IVACategory bIVACategory : mIVACategory){
			mIVACategoryDTOList.add(this.iMapper.map(bIVACategory,IVACategoryDTO.class));
		}
		
		return mIVACategoryDTOList;
	}
		
	// FIN PERSONAS
	
	//=======================================================================================
	
	// CLIENTES
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Proveedores.
	 * Un proveedor es una persona legal que tiene asociados productos, y que además
	 * está activo.
	 * @return lista de Persona Legal.
	 * @throws BusinessException
	 */
	public List<PersonDTO> getClients() throws BusinessException {
		LegalPersonService mLegalPersonService = new LegalPersonService();
		NaturalPersonService mNaturalPersonService = new NaturalPersonService();
		
		Iterable<LegalPerson> mLegalPerson = mLegalPersonService.getAll();
		
		Iterable<NaturalPerson> mNaturalPerson = mNaturalPersonService.getAll();
		
		List<PersonDTO> mClientsList = new ArrayList<PersonDTO>();
		
		for (LegalPerson bLegalPerson : mLegalPerson){
			if(bLegalPerson.isClient() && bLegalPerson.isActive())
			mClientsList.add(this.iMapper.map(bLegalPerson, LegalPersonDTO.class));
		}
		
		for (NaturalPerson bNaturalPerson : mNaturalPerson){
			if(bNaturalPerson.isActive())
			mClientsList.add(this.iMapper.map(bNaturalPerson, NaturalPersonDTO.class));
		}
		
		return mClientsList;
	}
	
	/**
	 * Determina si el cliente tiene deuda o no. Si el total adeudado es menor o igual
	 * a la suma de los pagos no descontados del cliente, entonce no tiene deuda.
	 * @param pClient el cliente del cual se quiere cancelar la deuda.
	 * @throws BusinessException 
	 */
	public Boolean clientIsInDebt(Person pClient) throws BusinessException{
		SaleService mSaleService = new SaleService();
		ProductService mProductService = new ProductService();
		
		//Recupero las ventas adeudas del cliente.
		List<Sale> mDueSales = mSaleService.getDueSalesByClientId(pClient.getId());
		Iterator<Sale> mDueSalesIterator = mDueSales.iterator();
				
		//Defino una variable para guardar la deuda total del cliente
		BigDecimal mDebt = BigDecimal.ZERO;
				
		// Calculo la deuda total del cliente
		while(mDueSalesIterator.hasNext()){ //Por cada venta adeudada
			Sale mSale = mDueSalesIterator.next();
			Iterator<SaleLine> bSaleLinesIterator = mSale.getSaleLines().iterator();
			while(bSaleLinesIterator.hasNext()){//Por cada línea de venta
				SaleLine bSaleLine = bSaleLinesIterator.next();
						
				//Convierto la cantidad de la línea a Bigdecimal
				BigDecimal bProductQuantity = BigDecimal.valueOf(bSaleLine.getQuantity());
						
				//Obtengo el precio unitario actual del producto de la línea
				BigDecimal bProductPrice = mProductService.get(bSaleLine.getProduct().getId()).getUnitPrice();
						
				//Sumo a la deuda la cantidad del producto por su precio unitario
				mDebt = mDebt.add(bProductQuantity.multiply(bProductPrice));
				
				//Resto el descuento hecho en la saleline
				//Obtengo el valor BigDecimal de 100
				BigDecimal bBigDecimal100 = BigDecimal.valueOf(100);
				//Multiplico la cantidad del producto por el precio guardado en el saleline 
				BigDecimal bSaleLineTotal = bProductQuantity.multiply(bProductPrice);
				//Calculo el descuento como: (cantidad*precio*descuento)/100
				BigDecimal bDiscount = bSaleLineTotal.multiply(bSaleLine.getDiscount()).divide(bBigDecimal100); 
				//Resto el descuento
				mDebt = mDebt.subtract(bDiscount);
			}
					
		}
				
		//La deuda del cliente es menor o igual que los pagos hechos?
		return (mDebt.compareTo(pClient.totalPaid()) == 1);
	}
	
	/**
	 * Cancela la deuda de un cliente, es decir, pone todas las ventas asociadas
	 * como pagadas.
	 * @param pClient el cliente del cual se quiere cancelar la deuda.
	 * @throws BusinessException 
	 */
	public void cancelClientDebt(Person pClient) throws BusinessException{
		SaleService mSaleService = new SaleService();
		
		//Marco las ventas como pagas
		mSaleService.payClientSales(pClient.getId());
	}
		
	// FIN CLIENTES
	
	//=======================================================================================
	
	// PROVEEDORES
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Proveedores.
	 * Un proveedor es una persona legal que tiene asociados productos, y que además
	 * está activo.
	 * @return lista de Persona Legal.
	 * @throws BusinessException
	 */
	public List<LegalPersonDTO> getSuppliers() throws BusinessException {
		LegalPersonService mLegalPersonService = new LegalPersonService();
		
		Iterable<LegalPerson> mLegalPerson = mLegalPersonService.getAll();
		
		List<LegalPersonDTO> mLegalPersonDTOList = new ArrayList<LegalPersonDTO>();
		
		for (LegalPerson bLegalPerson : mLegalPerson){
			if(bLegalPerson.isSupplier())
			mLegalPersonDTOList.add(this.iMapper.map(bLegalPerson,LegalPersonDTO.class));
		}
		
		return mLegalPersonDTOList;
	}
		
	// FIN PROVEEDORES
	
	//=======================================================================================
	
	// VENTAS
	
	/**
	 * Este método es un comando que permite guardar una Venta (SALE).
	 * 
	 * @param pSaleDTO venta a guardar
	 * @return identificador de la venta guardada
	 * @throws BusinessException 
	 */
	public Long createSale(SaleLiteDTO pSaleDTO) throws BusinessException {
		
		SaleService mSaleService = new SaleService();
		
		// map dto to domain object
		Sale mSale;
		if (pSaleDTO != null){
			
			
			mSale = iMapper.map(pSaleDTO, Sale.class);
			//Buscamos el Person para agregar el mSale
			
			PersonDTO mPersonDTO = this.getPerson(pSaleDTO.getPerson());
			Set<SaleLine> mSaleLineList = new HashSet<SaleLine>();
			
			for(SaleLineLiteDTO mSaleLineLiteDTO : pSaleDTO.getSaleLines()){
				SaleLine mSaleLine  = iMapper.map(mSaleLineLiteDTO, SaleLine.class);
				
				ProductDTO mProductDTO = this.getProduct(mSaleLineLiteDTO.getProduct());
				
				mSaleLine.setProduct(iMapper.map(mProductDTO, Product.class));
				
				mSaleLineList.add(mSaleLine);
			}
			mSale.setPerson(iMapper.map(mPersonDTO, Person.class));
			mSale.setSaleLines(mSaleLineList);
			//TODO - hAY QUE AGregar, el cliente al mSale. Y agregar el producto a los mSale.SaleLine que posea.
		} else {
			throw new BusinessException(SaleCons.cSALE_NULL_EXCEPTION_MESSAGE);
		}
		
		
		mSale = mSaleService.save(mSale);
		
		return mSale.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de ventas (Sale)
	 * @return lista de ventas (SaleDTO)
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<SaleDTO> getSales() throws BusinessException{
		SaleService mSaleService = new SaleService();
		
		Iterable<Sale> mSale = mSaleService.getAll();
		
		List<SaleDTO> mSaleDTOList = new ArrayList<SaleDTO>();
		
		for (Sale bSale : mSale){
			mSaleDTOList.add(this.iMapper.map(bSale,SaleDTO.class));
		}
		
		return mSaleDTOList;
	}
	
	/**
	 * Este método es una consulta que obtiene una venta (SALE) a partir de
	 * su identificador.
	 * @param pSaleId identificador del saleo
	 * @return venta encontrada
	 * @throws BusinessException la venta estaba eliminada lógicamente
	 */
	public SaleDTO getSale(Long pSaleId) throws BusinessException{
		SaleService mSaleService = new SaleService();
		
		Sale mSale = mSaleService.get(pSaleId);
		
		SaleDTO mSaleDTO = this.iMapper.map(mSale, SaleDTO.class);
		
		return mSaleDTO;
	}
	
	/**
	 * Este método es un comando que elimina una venta (SALE) a partir de su identificador.
	 * Al eliminar la venta (SALE), sus los lotes asociados son eliminados físicamente.
	 * @param pSaleId identificador de la venta (SALE) a eliminar
	 * @throws BusinessException errores de negocio al intentar hacer la operación
	 */
	public void deleteSale(Long pSaleId) throws BusinessException{
		SaleService mSaleService = new SaleService();
		
		// elimino el saleo
		mSaleService.delete(pSaleId);
		
	}
		
	// FIN VENTAS
	
	//=======================================================================================
		
}
