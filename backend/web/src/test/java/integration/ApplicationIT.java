package integration;


import org.junit.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.sql.ResultSet;

/**
 * Clase que contiene los tests de integración de toda la aplicación. Cada test
 * comprueba que dado un request, se devuelve el response esperado.
 * 
 * @author tomas
 *
 */
public class ApplicationIT extends ApplicationRestIT {

	@Test
	public void postProduct_happyPath() throws Exception {
		ResultSet mResultSet = super.executeDatabaseQuery("select (last_value+increment_by) AS next_value from product_id_seq");

		String mSequenceNextValue = null;
		if (mResultSet.next()) {
			mSequenceNextValue = mResultSet.getString("next_value");
		} else {
			fail("No se pudo obtener el próximo número de la secuencia");
		}
		
		super.performPost("/product", "/integration/post_product/onlyrequired_request")
				.andExpect(status().isOk())
				.andExpect(content().string(mSequenceNextValue));
	}
	
}
