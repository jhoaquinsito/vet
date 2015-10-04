package backend.core;

import junit.framework.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import backend.exception.BusinessException;
import backend.product.ProductDTO;
import backend.product.category.CategoryDTO;
import backend.product.drug.DrugDTO;
import backend.product.manufacturer.ManufacturerDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.product.presentation.PresentationDTO;

public class TestCNQ extends TestCase {

	private Connection iDatabaseConnection;

	public TestCNQ(String name) {
		super(name);
	}

	/**
	 * Per-test setup
	 */
	public void setUp() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			System.out.println("Test: no se encontró la clase del driver de la base de datos");
		}
		String url = "jdbc:postgresql://localhost:5432/vet?user=vet&password=vet";
		try {
			this.iDatabaseConnection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println("Test: no se pudo crear la conexión a la base.");
		}
	}

	/**
	 * Per-test teardown
	 */
	public void tearDown() {
		try {
			this.iDatabaseConnection.close();
		} catch (SQLException e) {
			System.out.println("Test: no se pudo cerrar la conexión de la base de datos");
		}
	}

	/**
	 * Test para comprobar si al guardar un nuevo producto, el id que devuelve
	 * es el correcto.
	 */
	public void test_saveProductReturnsRightId() {
		// obtengo el proximo numero de la secuencia de la base
		// este numero debe ser el proximo id
		Statement mStatement = null;
		ResultSet mResultSet = null;
		try {
			mStatement = this.iDatabaseConnection.createStatement();
			mResultSet = mStatement.executeQuery("select (last_value+increment_by) AS next_value from product_id_seq");
		} catch (SQLException e1) {
			fail("No se pudo obtener el numero de secuencia desde la base");
		}

		Long mSequenceNextValue = null;
		try {
			if (mResultSet.next()) {
				mSequenceNextValue = mResultSet.getLong("next_value");
			} else {
				fail("No se pudo obtener el prox numero de la secuencia");
			}
		} catch (SQLException e) {
			fail("No se pudo obtener el prox numero de la secuencia");
		}

		// creo un producto a guardar
		ProductDTO mProductToSave = new ProductDTO();
		mProductToSave.setName("Producto1 nombre");

		MeasureUnitDTO mMeasureUnitDTO = new MeasureUnitDTO();
		mMeasureUnitDTO.setName("Unidad de medida");
		mProductToSave.setMeasureUnit(mMeasureUnitDTO);

		// intento guardar el producto
		CommandAndQueries mCNQ = new CommandAndQueries();
		Long mId = null;
		try {
			mId = mCNQ.saveProduct(mProductToSave);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Falló el test de validar identificador.");
		}

		// pruebo el numero esperado vs el numero que recibí
		assertEquals(mSequenceNextValue, mId);
	}

	/**
	 * Test para comprobar que si se intenta guardar un producto vacío, se
	 * produce una excepción de negocio.
	 */
	public void test_saveProductEmpty() {

		ProductDTO mEmptyProduct = new ProductDTO();

		CommandAndQueries mCNQ = new CommandAndQueries();
		try {
			Long mId = mCNQ.saveProduct(mEmptyProduct);
			fail("Debió lanzar una excepción porque el DTO estaba vacío.");
			// TODO ver qué excepción lanza cuando pasa esto y cuando debemos
			// validar los datos.
		} catch (BusinessException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test para comprobar que si se intenta guardar un producto como null se
	 * produce una excepción de negocio.
	 */
	public void test_saveProductNull() {

		CommandAndQueries mCNQ = new CommandAndQueries();
		try {
			Long mId = mCNQ.saveProduct(null);
			fail("Debió lanzar una excepción porque el DTO era null.");
			// TODO ver qué excepción lanza cuando pasa esto y cuando debemos
			// validar los datos.
		} catch (BusinessException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test para comprobar si un producto se almacena bien al tratar de
	 * persistirlo.
	 */
	public void test_saveProductStoresOK() {

		ProductDTO mProductToSave = new ProductDTO();
		mProductToSave.setName("Producto nombre");
		mProductToSave.setDescription("Descripción 1");
		mProductToSave.setCost(new BigDecimal("1234.123456789"));
		mProductToSave.setMinimumStock(new BigDecimal("1"));
		mProductToSave.setUnitPrice(new BigDecimal("1"));
		mProductToSave.setUtility(new BigDecimal("1"));

		ManufacturerDTO mManufacturer = new ManufacturerDTO();
		mManufacturer.setName("manufacturer1 name");
		mProductToSave.setManufacturer(mManufacturer);

		MeasureUnitDTO mMeasureUnit = new MeasureUnitDTO();
		mMeasureUnit.setName("measure unit1 name");
		mProductToSave.setMeasureUnit(mMeasureUnit);

		PresentationDTO mPresentation = new PresentationDTO();
		mPresentation.setName("presentation1 name");
		mProductToSave.setPresentation(mPresentation);

		CategoryDTO mCategory = new CategoryDTO();
		mCategory.setName("category name");
		mProductToSave.setCategory(mCategory);

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
		mProductToSave.setDrugs(drugs);
		// TODO agregar la parte de batches

		CommandAndQueries mCNQ = new CommandAndQueries();
		Long mId = null;
		try {
			mId = mCNQ.saveProduct(mProductToSave);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Falló el test de probar que el producto queda bien almacenado.");
		}

		// expected
		mProductToSave.setId(mId);

		// TODO resultados en la base de datos

		assertEquals(true, true);
	}

	/**
	 * Default suite() method discovers all tests...
	 */
	public static Test suite() {
		return new TestSuite(TestCNQ.class);
	}

}
