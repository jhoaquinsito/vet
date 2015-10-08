package integration;


import org.junit.Test;
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
	public void postProduct_withoutRequiredFields() throws Exception {
		
		super.performPost("/product", "/integration/post_product/withoutrequiredfields_request")
				.andExpect(status().isConflict());
				//.andExpect(content().string(mSequenceNextValue));
	}
	
}
