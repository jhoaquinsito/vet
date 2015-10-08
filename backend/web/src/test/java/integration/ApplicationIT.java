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
	}
	
	@Test
	public void postProduct_emptyJSON() throws Exception {
		
		super.performPost("/product", "/integration/post_product/emptyjson_request")
				.andExpect(status().isConflict());
	}
	
	@Test
	public void deleteProduct() throws Exception {
		// TODO verificar en la base/archivo, si elimino el producto
		super.performDelete("/product/71")
				.andExpect(status().isOk());
	}
	
	@Test
	public void deleteProduct_whichNotExists() throws Exception {
		super.performDelete("/product/1111111")
				.andExpect(status().isConflict());
	}
	
	@Test
	public void getProduct() throws Exception {
		// TODO traer el producto de la base/archivo
		
		// TODO comparar el producto de la base con el que trajo
		
		super.performGet("/product/72")
				.andExpect(status().isOk());
	}
	
	@Test
	public void getProduct_whichNotExists() throws Exception {
		super.performGet("/product/1111111")
				.andExpect(status().isConflict());
	}
	
}
