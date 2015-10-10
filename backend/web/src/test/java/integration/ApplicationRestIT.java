package integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import integration.DatabaseUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@DatabaseSetup("file:src/test/resources/initial-dataset.xml")
public abstract class ApplicationRestIT extends TestCase {

	@Autowired
	private WebApplicationContext iWebApplicationContext;
	private MockMvc iMockMvc;
	
	public static final String cAPPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";
	
	@Before
	public void setup() throws Exception {
		
		this.iMockMvc = MockMvcBuilders.webAppContextSetup(this.iWebApplicationContext).build();
		
		// TODO databaseutils debería ser un atributo de esta clase
		DatabaseUtils.start();
		
	}

	/**
	 * Per-test teardown
	 */
	public void tearDown() throws Exception {
		
		DatabaseUtils.shutdown();
		
	}
	
	protected ResultSet executeDatabaseQuery(String pQuery) throws Exception {
		return DatabaseUtils.executeQuery(pQuery);
	}
	
	protected ResultActions performPost(String pURL, String pJsonRequestPath) throws Exception{
	return this.iMockMvc.perform(
			MockMvcRequestBuilders.post(pURL)
			.contentType(ApplicationRestIT.cAPPLICATION_JSON_UTF_8)
			.accept(MediaType.parseMediaType(ApplicationRestIT.cAPPLICATION_JSON_UTF_8))
			.content(this.readJsonFile(pJsonRequestPath)));
	}


	protected String readJsonFile(String pJsonFilePath) throws Exception {
		// leo todas las lineas del archivo de ejemplo
		List<String> mJsonFileLines = new ArrayList<String>();
		java.net.URL mUrl = getClass().getResource(pJsonFilePath);
		java.net.URI mUri = mUrl.toURI();
		Path mPath = Paths.get(mUri);
		mJsonFileLines = Files.readAllLines(mPath, StandardCharsets.UTF_8);
		
		// convierto la lista de lineas en un solo string
		String mJsonAsString = "";
		for (String bJsonFileLine : mJsonFileLines){
			mJsonAsString = mJsonAsString + bJsonFileLine;
		}
		
		return mJsonAsString;
	}
	
	protected String getNextValueFromSequence(String pSequenceName) throws Exception {
		ResultSet mResultSet = this.executeDatabaseQuery("select (last_value+increment_by) AS next_value from "+ pSequenceName);

		String mSequenceNextValue = null;
		if (mResultSet.next()) {
			mSequenceNextValue = mResultSet.getString("next_value");
		} else {
			fail("No se pudo obtener el próximo número de la secuencia");
		}
		return mSequenceNextValue;
	}
	
	protected ResultActions performDelete(String pURL) throws Exception{
		return this.iMockMvc.perform(
				MockMvcRequestBuilders.delete(pURL)
				.contentType(ApplicationRestIT.cAPPLICATION_JSON_UTF_8));
	}
	
	protected ResultActions performGet(String pURL) throws Exception {
		return this.iMockMvc.perform(
				MockMvcRequestBuilders.get(pURL)
				.contentType(ApplicationRestIT.cAPPLICATION_JSON_UTF_8));
	}
}
