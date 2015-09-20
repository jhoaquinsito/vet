package backend.core;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductRepository;
import backend.product.category.CategoryDTO;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.PresentationDTO;
import backend.utils.OrikaMapperFactory;
import junit.framework.*;

public class TestCNQ extends TestCase {
	
	public TestCNQ(String name) {
		super(name);
	}

	/**
	 * Per-test setup
	 */
	public void setUp() {
	}

	/**
	 * Per-test teardown
	 */
	public void tearDown() {
	}

	/*
	 * Tests go here...
	 */
//	public void testSaveProductReturnsRightId() {
//		
//		ProductDTO prod1 = new ProductDTO();
//		prod1.setId(Long.getLong("1"));
//		prod1.setName("Producto1 nombre");
//		prod1.setDescription("Descripción 1");
//		prod1.setCost(new BigDecimal("1"));
//		prod1.setDeletedOn(Timestamp.valueOf(LocalDateTime.now()));
//		prod1.setLastUpdateOn(Timestamp.valueOf(LocalDateTime.now()));
//		prod1.setLastUpdateUser("current user");
//		prod1.setMinimumStock(new BigDecimal("1"));
//		prod1.setUnitPrice(new BigDecimal("1"));
//		prod1.setUtility(new BigDecimal("1"));
//		
//		ManufacturerDTO mManufacturer = new ManufacturerDTO();
//		mManufacturer.setName("manufacturer1 name");
//		prod1.setManufacturer(mManufacturer);
//		
//		MeasureUnitDTO mMeasureUnit = new MeasureUnitDTO();
//		mMeasureUnit.setName("measure unit1 name");
//		prod1.setMeasureUnit(mMeasureUnit);
//		
//		PresentationDTO mPresentation = new PresentationDTO();
//		mPresentation.setName("presentation1 name");
//		prod1.setPresentation(mPresentation);
//		
//		CategoryDTO mCategory = new CategoryDTO();
//		mCategory.setName("category name");
//		prod1.setCategory(mCategory);
//
//		Set<DrugDTO> drugs = new HashSet<DrugDTO>();
//		DrugDTO droga1 = new DrugDTO();
//		droga1.setName("droga1-prod.creation-1");
//		DrugDTO droga2 = new DrugDTO();
//		droga2.setName("droga2-prod.creation-1");
//		DrugDTO droga3 = new DrugDTO();
//		droga3.setName("droga3-prod.creation-1");
//		drugs.add(droga1);
//		drugs.add(droga2);
//		drugs.add(droga3);
//		prod1.setDrugs(drugs);
//		
//		CommandAndQueries mCNQ = new CommandAndQueries();
//		Long mId = mCNQ.saveProduct(prod1);
//		
//		assertEquals((Long)1L, mId);
//	}
	
	public void testSaveProductEmpty() {
		
		ProductDTO prod1 = new ProductDTO();
		
		CommandAndQueries mCNQ = new CommandAndQueries();
		try {
			Long mId = mCNQ.saveProduct(prod1);
			fail("Debió lanzar una excepción porque el DTO estaba vacío.");
			// TODO ver qué excepción lanza cuando pasa esto y cuando debemos validar los datos.
		} catch (Exception e){
			assertTrue(true);
		}
	}
	
	public void testSaveProductStoresOK() {
		
		ProductDTO prod1 = new ProductDTO();
		prod1.setName("Producto1 f22 fsdfouasndfansdfnaisdnf123123123asjndflkjsndfioausndofunsidnfiasodnufoiaunsodiunfiasdfnombre");
		prod1.setDescription("Descripción 1");
		prod1.setCost(new BigDecimal("1234.123456789"));
		prod1.setDeletedOn(Timestamp.valueOf(LocalDateTime.now()));
		prod1.setLastUpdateOn(Timestamp.valueOf(LocalDateTime.now()));
		prod1.setLastUpdateUser("current user");
		prod1.setMinimumStock(new BigDecimal("1"));
		prod1.setUnitPrice(new BigDecimal("1"));
		prod1.setUtility(new BigDecimal("1"));
		
		ManufacturerDTO mManufacturer = new ManufacturerDTO();
		mManufacturer.setName("manufacturer1 name");
		prod1.setManufacturer(mManufacturer);
		
		MeasureUnitDTO mMeasureUnit = new MeasureUnitDTO();
		mMeasureUnit.setName("measure unit1 name");
		prod1.setMeasureUnit(mMeasureUnit);
		
		PresentationDTO mPresentation = new PresentationDTO();
		mPresentation.setName("presentation1 name");
		prod1.setPresentation(mPresentation);
		
		CategoryDTO mCategory = new CategoryDTO();
		mCategory.setName("category name");
		prod1.setCategory(mCategory);

		Set<DrugDTO> drugs = new HashSet<DrugDTO>();
		DrugDTO droga1 = new DrugDTO();
		droga1.setName("droga1-prod.creation-1");
		DrugDTO droga2 = new DrugDTO();
		droga2.setName("droga2-prod.creation-1");
		DrugDTO droga3 = new DrugDTO();
		droga3.setName("droga3-prod.creation-1");
		drugs.add(droga1);
		drugs.add(droga2);
		drugs.add(droga3);
		prod1.setDrugs(drugs);
		
		CommandAndQueries mCNQ = new CommandAndQueries();
		Long mId = mCNQ.saveProduct(prod1);
		
		
		// expected
		prod1.setId(mId);
		Product mProductExpected = OrikaMapperFactory.getMapperFacade().map(prod1, Product.class);
		
		//result 
		ApplicationContext mAppContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		ProductRepository mProductRepository = (ProductRepository) mAppContext.getBean(ProductRepository.class);
		Product mProductResult = mProductRepository.findOne(mId);
		
		// TODO tendría que validar de que el DTO sea identico al que llegó
		// estoy validando entre dos Product y debería validar entre dos DTO
		
		assertEquals(mProductExpected, mProductResult);
	}	

	/**
	 * Default suite() method discovers all tests...
	 */
	public static Test suite() {
		return new TestSuite(TestCNQ.class);
	}
	
}
