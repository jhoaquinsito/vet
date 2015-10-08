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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
public abstract class ApplicationRestIT extends TestCase {

	@Autowired
	private WebApplicationContext iWebApplicationContext;
	private MockMvc iMockMvc;
	private Connection iDatabaseConnection;
	
	public static final String cAPPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";
	
	@Before
	public void setup() throws Exception {
		this.iMockMvc = MockMvcBuilders.webAppContextSetup(this.iWebApplicationContext).build();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			throw new ClassNotFoundException("Test: no se encontró la clase del driver de la base de datos", e1);
		}
		String url = "jdbc:postgresql://localhost:5432/vet?user=vet&password=vet";
		try {
			this.iDatabaseConnection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new SQLException("Test: no se pudo crear la conexión a la base.", e);
		}
	}

	/**
	 * Per-test teardown
	 */
	public void tearDown() throws Exception {
		try {
			this.iDatabaseConnection.close();
		} catch (SQLException e) {
			throw new SQLException("Test: no se pudo cerrar la conexión de la base de datos", e);
		}
	}
	

	protected ResultSet executeDatabaseQuery(String pQuery) throws Exception {
		Statement mStatement = this.iDatabaseConnection.createStatement();
		return mStatement.executeQuery(pQuery);
	}
	
	protected ResultActions performPost(String pURL, String pJsonRequestPath) throws Exception{
	return this.iMockMvc.perform(
			post(pURL)
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
	
	
}
