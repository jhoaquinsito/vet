package backend.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import backend.person.settlement.Settlement;
import backend.person.settlement.SettlementDTO;
import backend.product.BatchCodeGenerator;
import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductForSaleDTO;
import backend.product.ProductService;
import backend.product.batch.Batch;
import backend.product.batch.BatchDTO;
import backend.product.batch.BatchService;
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
import backend.utils.EntityValidator;
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
@Component
public class CommandAndQueries {
	// MapperFacade de Orika (librería para mapping)
	private MapperFacade iMapper; 

	@Autowired private CategoryService iCategoryService;
	@Autowired private ProductService iProductService;
	@Autowired private PresentationService iPresentationService;
	@Autowired private ManufacturerService iManufacturerService;
	@Autowired private MeasureUnitService iMeasureUnitService;
	@Autowired private DrugService iDrugService;
	@Autowired private LegalPersonService iLegalPersonService;
	@Autowired private NaturalPersonService iNaturalPersonService;
	@Autowired private SaleService iSaleService;
	@Autowired private PersonService iPersonService;
	@Autowired private IVACategoryService iIVACategoryService;
	@Autowired private BatchService iBatchService;
	
	private static final String cPRODUCT_NULL_EXCEPTION_MESSAGE = "El producto no tiene valores.";
	private static final String cNATURAL_PERSON_NULL_EXCEPTION_MESSAGE = "La persona física no tiene valores válidos.";
	private static final String cINSUFFICIENT_STOCK_EXCEPTION_MESSAGE = "La cantidad disponible del producto es insuficiente: ";
	
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
	 * Método que almacena un conjunto de productos.
	 * 
	 * @param pBunchOfProductDTOs conjunto de productos
	 * @throws BusinessException errores de negocio
	 */
	public void saveBunchOfProducts(List<ProductDTO> pBunchOfProductDTOs) throws BusinessException {
		if (pBunchOfProductDTOs != null){
			// map dto to domain object
			List<Product> mBunchOfProducts = this.iMapper.mapAsList(pBunchOfProductDTOs, Product.class);
			// save all
			this.iProductService.save(mBunchOfProducts);
		}
	}
	
	/**
	 * Este método es un comando que permite guardar un producto.
	 * 
	 * @param pProductDTO producto a guardar
	 * @return identificador del producto guardado
	 * @throws BusinessException 
	 */
	// TODO cambiar nombre de este método por "createProduct"
	public Long saveProduct(ProductDTO pProductDTO) throws BusinessException {
		
		
		// map dto to domain object
		Product mProduct;
		if (pProductDTO != null){
			mProduct = iMapper.map(pProductDTO, Product.class);
		} else {
			throw new BusinessException(CommandAndQueries.cPRODUCT_NULL_EXCEPTION_MESSAGE);
		}
		
		
		mProduct = this.iProductService.save(mProduct);
		
		return mProduct.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Productos
	 * @return lista de Producto
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<ProductDTO> getProducts() throws BusinessException{
		
		Iterable<Product> mProduct = this.iProductService.getAll();
		
		List<ProductDTO> mProductDTOList = new ArrayList<ProductDTO>();
		
		for (Product bProduct : mProduct){
			mProductDTOList.add(this.iMapper.map(bProduct,ProductDTO.class));
		}
		
		return mProductDTOList;
	}
	
	/**
	 * Este método es una consulta que devuelve la lista de productos
	 * cuyos batch estén próximos a una fecha de vencimiento, especificados
	 * por los parámetros de entrada.
	 * Solamente devuelven aquellos productos cuyos batch entran en las condiciones.
	 * Batches de productos que no entran, no se envian con el producto (en el caso que el producto tenga múltiples batch
	 * y algunos estén en las condiciones y otros no)
	 * @param pBeginDate:
	 * 					 Fecha de inicio para empezar a contar como cota, por lo general: Fecha actual
	 * @param pDays:
	 * 				Cantidad de días a contar a partir de la fecha de inicio, para determinar el rango de los productos que entran.
	 * @return
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<BatchDTO> getBatchesByDueDateAndDays(Date pBeginDate, Integer pDays) throws BusinessException{
		
		List<Batch> mBatch = this.iBatchService.getListByDueDate(pBeginDate, pDays);
		
		List<BatchDTO> mBatchDTOList = new ArrayList<BatchDTO>();
		
		for (Batch bBatch : mBatch){
			mBatchDTOList.add(this.iMapper.map(bBatch,BatchDTO.class));
		}
		
		return mBatchDTOList;
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Productos
	 * filtrados a traves de su:
	 * @param pProductName : Fragmento de nombre de un producto
	 * @return
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<ProductDTO> getProductsByName(String pProductName) throws BusinessException{
		
		Iterable<Product> mProduct = this.iProductService.getProductListByName(pProductName);
		
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
		
		Product mProduct = this.iProductService.get(pProductId);
		
		ProductDTO mProductDTO = this.iMapper.map(mProduct, ProductDTO.class);
		
		return mProductDTO;
	}
	
	/**
	 * Este método es una consulta que obtiene un producto a partir de
	 * un código de lote seleccionado para la venta.
	 * @param pProductBatchCode un código de lote seleccionado para la venta
	 * @return mProductForSaleDTO producto a vender
	 * @throws BusinessException
	 */
	public ProductForSaleDTO getProductForSaleByBatchCode(String pSelectedProductBatchCode) throws BusinessException{
		
		Long mProductForSaleId = BatchCodeGenerator.getProductId(pSelectedProductBatchCode);
		
		Integer mSelectedBatchISODueDate = BatchCodeGenerator.getBatchISODueDate(pSelectedProductBatchCode);
		
		Product mProduct = this.iProductService.get(mProductForSaleId);
		
		ProductForSaleDTO mProductForSaleDTO = this.iMapper.map(mProduct, ProductForSaleDTO.class);
		
		Batch mBatchToSale = mProduct.getBatcheByISODueDate(mSelectedBatchISODueDate);
		
		if (mBatchToSale != null && mBatchToSale.getId()!= null){
			mProductForSaleDTO.setBatchId(mBatchToSale.getId());
		} else {
			throw new BusinessException("El lote a vender no existe en el producto.");
		}
		
		mProductForSaleDTO.setBatchIsoDueDate(mSelectedBatchISODueDate);
		
		return mProductForSaleDTO;
	}
	
	/**
	 * Este método es un comando que elimina un producto a partir de su identificador.
	 * Al eliminar el producto, sus los lotes asociados son eliminados físicamente.
	 * @param pProductId identificador del producto a eliminar
	 * @throws BusinessException errores de negocio al intentar hacer la operación
	 */
	public void deleteProduct(Long pProductId) throws BusinessException{
		
		// elimino el producto
		this.iProductService.delete(pProductId);
		
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Presentaciones.
	 * @return lista de Presentaciones
	 * @throws BusinessException
	 */
	public List<PresentationDTO> getPresentations() throws BusinessException{
		
		Iterable<Presentation> mPresentations = this.iPresentationService.getAll();
		
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
		
		Iterable<MeasureUnit> mMeasureUnits = this.iMeasureUnitService.getAll();
		
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
		
		Iterable<Category> mCategory = this.iCategoryService.getAll();
		
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
		
		Category bCategory = new Category();
		
		bCategory = this.iMapper.map(pCategory,Category.class);
		
		return this.iMapper.map(this.iCategoryService.save(bCategory),CategoryDTO.class).getId();
		
	}

	/**
	 * Este método es una consulta que devuelve la lista completa de laboratorios
	 * @return lista de laboratorios
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public List<ManufacturerDTO> getManufacturers() throws BusinessException{
		
		Iterable<Manufacturer> mManufacturer = this.iManufacturerService.getAll();
		
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
		
		Manufacturer bManufacturer = new Manufacturer();
		
		bManufacturer = this.iMapper.map(pManufacturer,Manufacturer.class);
		
		return this.iMapper.map(this.iManufacturerService.save(bManufacturer),ManufacturerDTO.class).getId();
		
	}

	/**
	 * Este método es un comando que permite guardar una presentación.
	 * @param pPresentationDTO presentación a guardar
	 * @return identificador de la presentación guardada
	 * @throws BusinessException errores de negocio producidos
	 */
	public Long createPresentation(PresentationDTO pPresentationDTO) throws BusinessException{
		
		// map dto to domain object
		Presentation mPresentation = iMapper.map(pPresentationDTO, Presentation.class);
		
		mPresentation = this.iPresentationService.create(mPresentation);
		
		return mPresentation.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de drogas
	 * @return lista de drogas
	 */
	public List<DrugDTO> getDrugs(){
		
		Iterable<Drug> mDrugs = this.iDrugService.getAll();
		
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
		
		// map dto to domain object
		Drug mDrug = iMapper.map(pDrugDTO, Drug.class);
		
		mDrug = this.iDrugService.create(mDrug);
		
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
		
		Iterable<LegalPerson> mLegalPersonList = this.iLegalPersonService.getAll();
		
		Iterable<NaturalPerson> mNaturalPersonList = this.iNaturalPersonService.getAll();
		
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
		
		
		Person mPerson = this.iPersonService.get(pPersonId);
		
		return this.iMapper.map(mPerson,PersonDTO.class);
	}
	
	/**
	 * Este método es un comando que elimina una Persona a partir de su identificador.
	 * La eliminación es de tipo Lógica.
	 * @param pPersonId identificador de la persona asociada.
	 * @throws BusinessException errores de negocio al intentar hacer la operación
	 */
	public void deletePerson(Long pPersonId) throws BusinessException {
		// elimino la persona
		this.iPersonService.delete(pPersonId);
	}
	
	/**
	 * Este método es un comando que permite guardar un LegalPerson
	 * @param pLegalPersonDTO : Persona legal a guardar.
	 * @return identificador de la Persona que es una Persona Legal.
	 * @throws BusinessException : Excepción con detalles de los errores de negocio
	 */
	public Long saveLegalPerson(LegalPersonDTO pLegalPersonDTO) throws BusinessException {
		// map dto to domain object
		LegalPerson mLegalPerson = iMapper.map(pLegalPersonDTO, LegalPerson.class);
		
		//Si el cliente no es nuevo y tiene ventas adeudadas
		if(mLegalPerson.getId() != null && !this.clientIsInDebt(mLegalPerson)){
			
			
			Settlement surplusSettlement = new Settlement();
			
			//Analizo si el total pagado excede el total de la deuda, caso afirmativo, creamos un Settlement con el excedente.
			if( mLegalPerson.totalPaid().compareTo(this.getClientDebt(mLegalPerson)) > 0 ){
				
				//Reestablezco la lista Settlements
				mLegalPerson.setSettlements(mLegalPerson.getSettlementListWithSurplus(this.getClientDebt(mLegalPerson)));
				
				surplusSettlement = ((TreeSet<Settlement>) mLegalPerson.getSettlements()).last();
				
				//Removemos el último elemento.
				mLegalPerson.getSettlements().remove(surplusSettlement);
				
				//Descuento los pagos del cliente
				mLegalPerson.discountSettlements();
				
				//Agrego el nuevo Settlement NO descontado a la person
				mLegalPerson.addSettlement(surplusSettlement);
			}else{
				//Descuento los pagos del cliente
				mLegalPerson.discountSettlements();
			}
			
			//Cancelo la deuda del cliente
			this.cancelClientDebt(mLegalPerson);
		}
		
		mLegalPerson = this.iLegalPersonService.save(mLegalPerson);
		
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
		Iterable<LegalPerson> mLegalPerson = this.iLegalPersonService.getAll();
		
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
		LegalPerson mLegalPerson = this.iLegalPersonService.get(pLegalPersonId);
		
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
		// map dto to domain object
		NaturalPerson mNaturalPerson;
		if (pNaturalPersonDTO != null){
			mNaturalPerson = iMapper.map(pNaturalPersonDTO, NaturalPerson.class);
		} else {
			throw new BusinessException(CommandAndQueries.cNATURAL_PERSON_NULL_EXCEPTION_MESSAGE);
		}
		
		//Si el cliente no es nuevo y tiene ventas adeudadas
		if(pNaturalPersonDTO.getId() != null && !this.clientIsInDebt(mNaturalPerson)){
			
			Settlement surplusSettlement = new Settlement();
			
			//Analizo si el total pagado excede el total de la deuda, caso afirmativo, creamos un Settlement con el excedente.
			if( mNaturalPerson.totalPaid().compareTo(this.getClientDebt(mNaturalPerson)) > 0 ){
				
				//Reestablezco la lista Settlements
				mNaturalPerson.setSettlements(mNaturalPerson.getSettlementListWithSurplus(this.getClientDebt(mNaturalPerson)));
				
				surplusSettlement = ((TreeSet<Settlement>) mNaturalPerson.getSettlements()).last();
				
				//Removemos el último elemento.
				mNaturalPerson.getSettlements().remove(surplusSettlement);
				
				//Descuento los pagos del cliente
				mNaturalPerson.discountSettlements();
				
				//Agrego el nuevo Settlement NO descontado a la person
				mNaturalPerson.addSettlement(surplusSettlement);
			}else{
				//Descuento los pagos del cliente
				mNaturalPerson.discountSettlements();
			}
			
			//Cancelo la deuda del cliente
			this.cancelClientDebt(mNaturalPerson);
		}
		
		mNaturalPerson = this.iNaturalPersonService.save(mNaturalPerson);
		
		return mNaturalPerson.getId();
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Personas Físicas
	 * @return lista de Personas Físicas
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<Object> getNaturalPeople() throws BusinessException{
		Iterable<NaturalPerson> mNaturalPersonList = this.iNaturalPersonService.getAll();
		
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
		NaturalPerson mNaturalPerson = this.iNaturalPersonService.get(pPersonId);
		
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
		IVACategory mIVACategory = new IVACategory();
		
		mIVACategory = this.iMapper.map(pIVACategory,IVACategory.class);
		
		return this.iMapper.map(this.iIVACategoryService.save(mIVACategory),IVACategoryDTO.class).getId();
		
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de Categorías de IVA
	 * @return lista de Categorias de IVA
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<IVACategoryDTO> getIVACategories() throws BusinessException{
		Iterable<IVACategory> mIVACategory = this.iIVACategoryService.getAll();
		
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
	 * Este método es una consulta que devuelve la lista completa de clientes activos (naturales o legales).
	 * 
	 * @return lista de clientes.
	 * @throws BusinessException
	 */
	public List<PersonDTO> getClients() throws BusinessException {
		Iterable<LegalPerson> mLegalPerson = this.iLegalPersonService.getAll();
		
		Iterable<NaturalPerson> mNaturalPerson = this.iNaturalPersonService.getAll();
		
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
	
	/***
	 * Este método permite calcular la deuda de un cliente.
	 * @param pClient
	 * @return
	 * @throws BusinessException
	 */
	public BigDecimal getClientDebt(Person pClient) throws BusinessException{
		
		
		//Recupero las ventas adeudas del cliente.
		List<Sale> mDueSales = this.iSaleService.getDueSalesByClientId(pClient.getId());
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
				BigDecimal bProductPrice = this.iProductService.get(bSaleLine.getBatch().getProduct().getId()).getUnitPrice();

						
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
				
		return mDebt;
	}
	
	/**
	 * Determina si el cliente tiene deuda o no. Si el total adeudado es menor o igual
	 * a la suma de los pagos no descontados del cliente, entonce no tiene deuda.
	 * @param pClient el cliente del cual se quiere cancelar la deuda.
	 * @throws BusinessException 
	 */
	public Boolean clientIsInDebt(Person pClient) throws BusinessException{
		
		//Defino una variable para guardar la deuda total del cliente
		BigDecimal mDebt = BigDecimal.ZERO;
				
		// Calculo la deuda total del cliente
		mDebt = getClientDebt(pClient);
			
		//La deuda del cliente es menor o igual que los pagos hechos?
		return (mDebt.compareTo(pClient.totalPaid()) == 1);
	}
	
	public void setClientSettlements(Long pClientId, List<SettlementDTO> pUpdatedSettlements) throws BusinessException{
		
		Set<Settlement> mUpdatedClientSettlements = this.iMapper.mapAsSet(pUpdatedSettlements, Settlement.class);
		
		this.iPersonService.setClientSettlements(pClientId, mUpdatedClientSettlements);
	}
	
	/**
	 * Cancela la deuda de un cliente, es decir, pone todas las ventas asociadas
	 * como pagadas.
	 * @param pClient el cliente del cual se quiere cancelar la deuda.
	 * @throws BusinessException 
	 */
	public void cancelClientDebt(Person pClient) throws BusinessException{
		//Marco las ventas como pagas
		this.iSaleService.payClientSales(pClient.getId());
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
		Iterable<LegalPerson> mLegalPerson = this.iLegalPersonService.getAll();
		
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
		
		EntityValidator mEntityValidator = new EntityValidator();
		// map dto to domain object
		Sale mSale;
		if (pSaleDTO != null){
			if(pSaleDTO.getSettlement() == null)
				throw new BusinessException(SaleCons.cSALE_SETTLEMENT_NULL_EXCEPTION_MESSAGE);
			mSale = iMapper.map(pSaleDTO, Sale.class);
			//Buscamos el Person para agregar el mSale
			
			if (pSaleDTO.getPerson() != null){
				PersonDTO mPersonDTO = this.getPerson(pSaleDTO.getPerson());
				Person mPerson = iMapper.map(mPersonDTO, Person.class);
				Settlement mSettlement = iMapper.map(pSaleDTO.getSettlement(), Settlement.class);
				if(mSettlement.getDate() == null ) mSettlement.setDate(new Date());
				
				//Valido Settlement
				mEntityValidator.validate(mSettlement);
				
				
				mPerson.addSettlement(mSettlement);
				mSale.setPerson(mPerson);
					
			}
			
			Set<SaleLine> mSaleLineList = new HashSet<SaleLine>();
			
			for(SaleLineLiteDTO mSaleLineLiteDTO : pSaleDTO.getSaleLines()){
				SaleLine mSaleLine  = iMapper.map(mSaleLineLiteDTO, SaleLine.class);
				
				BatchDTO bBatchDTO = this.getBatch(mSaleLineLiteDTO.getBatch());
				
				Batch bBatch = iMapper.map(bBatchDTO, Batch.class);
				
				//Descuento el stock en el lote correspondiente 
				BigDecimal bQuantity = BigDecimal.valueOf(mSaleLineLiteDTO.getQuantity());
				BigDecimal bNewStock = bBatch.getStock().subtract(bQuantity);
				if(bNewStock.compareTo(BigDecimal.ZERO) >= 0){
					bBatch.setStock(bNewStock);
				}else{
					throw new BusinessException(CommandAndQueries.cINSUFFICIENT_STOCK_EXCEPTION_MESSAGE + bBatch.getProduct().getName());
				}
				
				mSaleLine.setBatch(bBatch);
				
				mSaleLineList.add(mSaleLine);
			}
			
			mSale.setDate(new Date());
			
			mSale.setSaleLines(mSaleLineList);

		} else {
			throw new BusinessException(SaleCons.cSALE_NULL_EXCEPTION_MESSAGE);
		}
		
		
		mSale = this.iSaleService.save(mSale);
		
		return mSale.getId();
	}
	
	/** Este método retorna las ventas asociadas a un cliente, que aún no han sido pagadas.
	 * @param pClientId
	 * @return
	 * @throws BusinessException 
	 */
	public List<SaleDTO> getDueSalesByClientId(Long pClientId) throws BusinessException {
		
		List<SaleDTO> mSaleDTOList 	= new ArrayList<SaleDTO>();
		Iterable<Sale> mSale 		= this.iSaleService.getDueSalesByClientId(pClientId);
		
		for (Sale bSale : mSale){
			mSaleDTOList.add(this.iMapper.map(bSale,SaleDTO.class));
		}
		
		return mSaleDTOList;
	}
	
	/**
	 * Este método retorna las ventas que se encuentran bajo los parámetros especificados.
	 * @param pBeginDate:
	 * 						Fecha de inicio a considerar
	 * @param pEndDate:
	 * 						Fecha de fin a considerar
	 * @param pSalePayingForm:
	 * 						Forma de pago especificada, opciones: Todas, Efectivo o Cuenta Corriente
	 * @return
	 * 		  Devuelve una lista de SaleLiteDTO, es de tipo LITE pues no se necesitan todos los datos, 
	 *        al mismo tiempo que como puede ser un gran listado, puede generar excepcion por gran cantidad de datos.
	 * @throws BusinessException
	 */
	public List<SaleLiteDTO> getSalesForReport(Date pBeginDate,Date pEndDate, String pSalePayingForm) throws BusinessException{
		
		List<Sale> mSales = this.iSaleService.getReportSales(pBeginDate, pEndDate, pSalePayingForm);
		
		List<SaleLiteDTO> mSaleDTOList = new ArrayList<SaleLiteDTO>();
		
		for (Sale bSale : mSales){
			SaleLiteDTO mSaleLiteDTO = this.iMapper.map(bSale,SaleLiteDTO.class); 
			
			NaturalPerson mNaturalPerson = this.iNaturalPersonService.get(mSaleLiteDTO.getPerson());
			if(mNaturalPerson != null)
				mSaleLiteDTO.setPersonName(mNaturalPerson.getLastName() + ", " + mNaturalPerson.getName() );
			else{
				LegalPerson mLegalPerson = this.iLegalPersonService.get(mSaleLiteDTO.getPerson());
				if(mLegalPerson
						!= null)
					mSaleLiteDTO.setPersonName(mLegalPerson.getName() );
			}
				
			
			mSaleDTOList.add(mSaleLiteDTO);
			//Obtenemos el nombre de la persona
		}
		
		return mSaleDTOList;
	}
	
	/**
	 * Este método es una consulta que devuelve la lista completa de ventas (Sale)
	 * @return lista de ventas (SaleDTO)
	 * @throws BusinessException : Excepcion con detalles de los errores de negocio
	 */
	public List<SaleDTO> getSales() throws BusinessException{
		Iterable<Sale> mSale = this.iSaleService.getAll();
		
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
		Sale mSale = this.iSaleService.get(pSaleId);
		
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
		// elimino el saleo
		this.iSaleService.delete(pSaleId);
		
	}


	public BatchDTO getBatch(Long pBatchId) throws BusinessException {
			
		Batch mBatch = this.iBatchService.get(pBatchId);
		
		return this.iMapper.map(mBatch,BatchDTO.class);
	}
		
	// FIN VENTAS
	
	//=======================================================================================
		
}
