package integration;


import org.junit.Test;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Clase que contiene los tests de integración de toda la aplicación. Cada test
 * comprueba que dado un request, se devuelve el response esperado.
 * 
 * @author tomas
 *
 */
public class ApplicationIT extends ApplicationRestIT {

	@Test
	public void postProduct_onlyRequiredFields() throws Exception {
		
		String mSequenceNextValue = super.getNextValueFromSequence("product_id_seq");
		
		super.performPost("/product", "/integration/post_product/onlyrequired_request")
				.andExpect(status().isOk())
				.andExpect(content().string(mSequenceNextValue));
	}
	
	@Test
	public void postProduct_withAllFields() throws Exception {
		
		String mSequenceNextValue = super.getNextValueFromSequence("product_id_seq");
		
		super.performPost("/product", "/integration/post_product/withallfields_request")
				.andExpect(status().isOk())
				.andExpect(content().string(mSequenceNextValue));
	}
	
	@ExpectedDatabase(assertionMode= DatabaseAssertionMode.NON_STRICT, value="file:src/test/resources/initial-dataset.xml")
	@Test
	public void postProduct_withoutRequiredFields() throws Exception {
		
		super.performPost("/product", "/integration/post_product/withoutrequiredfields_request")
				.andExpect(status().isConflict());
	}
	
	@Test
	public void postProduct_emptyJSON() throws Exception {
		
		super.performPost("/product", "/integration/post_product/emptyjson_request")
				.andExpect(status().isConflict());
	}
	
	@ExpectedDatabase(assertionMode= DatabaseAssertionMode.NON_STRICT, value= "file:src/test/resources/data-product1-deleted.xml")
	@Test
	public void deleteProduct() throws Exception {
		// TODO verificar en la base/archivo, si elimino el producto
		super.performDelete("/product/1")
				.andExpect(status().isOk())
				.andExpect(content().string(""));
	}
	
	@Test
	public void deleteProduct_whichNotExists() throws Exception {
		super.performDelete("/product/123456789")
				.andExpect(status().isConflict());
	}
	
	@ExpectedDatabase(assertionMode= DatabaseAssertionMode.NON_STRICT, value="file:src/test/resources/initial-dataset.xml")
	@Test
	public void getProduct() throws Exception {
		
		// TODO comparar el producto de la base con el que trajo
		String mExpectedJSONResponse = super.readJsonFile("/integration/get_product/productone_response");
		
		super.performGet("/product/1")
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(content().json(mExpectedJSONResponse));
	}
	
	@Test
	public void getProduct_whichNotExists() throws Exception {
		super.performGet("/product/1111111")
				.andExpect(status().isConflict());
	}
	
}
