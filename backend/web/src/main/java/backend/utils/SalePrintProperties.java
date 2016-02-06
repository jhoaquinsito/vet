/**
 * 
 */
package backend.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import backend.exception.BusinessException;

/**
 * Esta clase representa los <strong> PARAMETROS DE IMPRESION DE UNA VENTA</strong>
 * Es una clase de tipo singleton, que cuando se instancia recupera los valores de los parametros del archivo: saleprint.properties
 * @author gonzalo
 *
 */

public class SalePrintProperties {

	   static SalePrintProperties instance = null;
	   private Properties  iProperties 	   = new Properties();
	   private InputStream iInputStream	   = null;
	
	   //TODO - En produccion o en las otras maquinas de desarrollo no olvidar de cambiar esta URL.
       private String iPropertiesFileName  = "C:/Users/gonzalo/git/jhoaquinsito/vet/backend/web/src/main/resources/properties/saleprint.properties";
       /* PROPIEDADES */
       
       public boolean HAS_COMPANY_HEADER_FIELD 	= false;
       public boolean HAS_CLIENT_NAME_FIELD 	= false;
       public boolean HAS_DATE_NOW_FIELD 		= false;
       public boolean HAS_PAYMENT_METHOD_FIELD 	= false;
       public boolean HAS_BOX_HEADDER 			= false;
       public boolean HAS_BREAKLINE_HEADER 		= false;
       public boolean HAS_BREAKLINE_PRODUCTS 	= false;
       public boolean HAS_BREAKLINE_TOTAL 		= false;
       public boolean HAS_BREAKLINE_GREETINGS 	= false;
       
       public String COMPANY_HEADER_VALUE 	 = "";
       public String DATE_NOW_PATTERN  		 = "";
       public String BOX_HEADER_COORDINATES  = "";
       
       public String X_ALIGN_LEFT 			 = "";
       public String X_ALIGN_COMPANY_NAME 	 = "";
       public String X_ALIGN_HEADER_VALUE    = "";
       public String X_ALIGN_ITEM_PRICE      = "";
       public String X_ALIGN_ITEM_DISC_PRICE = "";
       public String X_ALIGN_TOTAL_PRICE 	 = "";
       public String Y_START_VALUE 			 = "";
       
       public int    ITEM_NAME_MAX_CHARACTER = 0;
       /***************/
       
	   protected SalePrintProperties()  throws BusinessException {
		   try {
			loadSalePrintProperties();
		} catch (IOException e) {
			throw new BusinessException("Se ha producido la siguiente IOException: " + e.getMessage());
		}
	   }
	   public static SalePrintProperties getInstance() throws BusinessException {
	      if(instance == null) {
	         instance = new SalePrintProperties();
	      }
	      return instance;
	   }
	   
	   private void loadSalePrintProperties() throws BusinessException, IOException {
		   
			try {
				
				
				iInputStream = new FileInputStream(iPropertiesFileName);
				//iInputStream = getClass().getClassLoader().getResourceAsStream("web/src/main/resources/properties/saleprint.properties");
	 
				if (iInputStream != null) {
					iProperties.load(iInputStream);
				} else {
					throw new BusinessException("Archivo de propiedades nombre :'" + iPropertiesFileName + "' no fue encontrado en el classpath");
				}
	 
	 
				// get the iPropertieserty value and print it out
				this.HAS_COMPANY_HEADER_FIELD 		= Boolean.valueOf(iProperties.getProperty("HAS_COMPANY_HEADER_FIELD"));
				this.HAS_CLIENT_NAME_FIELD 			= Boolean.valueOf(iProperties.getProperty("HAS_CLIENT_NAME_FIELD"));
				this.HAS_DATE_NOW_FIELD 			= Boolean.valueOf(iProperties.getProperty("HAS_DATE_NOW_FIELD"));
				this.HAS_PAYMENT_METHOD_FIELD 		= Boolean.valueOf(iProperties.getProperty("HAS_PAYMENT_METHOD_FIELD"));
				this.HAS_BOX_HEADDER 				= Boolean.valueOf(iProperties.getProperty("HAS_BOX_HEADDER"));
				this.HAS_BREAKLINE_HEADER 			= Boolean.valueOf(iProperties.getProperty("HAS_BREAKLINE_HEADER"));
				this.HAS_BREAKLINE_PRODUCTS 		= Boolean.valueOf(iProperties.getProperty("HAS_BREAKLINE_PRODUCTS"));
				this.HAS_BREAKLINE_TOTAL 			= Boolean.valueOf(iProperties.getProperty("HAS_BREAKLINE_TOTAL"));
				this.HAS_BREAKLINE_GREETINGS 		= Boolean.valueOf(iProperties.getProperty("HAS_BREAKLINE_GREETINGS"));
				this.COMPANY_HEADER_VALUE 			= iProperties.getProperty("COMPANY_HEADER_VALUE");
				this.DATE_NOW_PATTERN 				= iProperties.getProperty("DATE_NOW_PATTERN");
				this.BOX_HEADER_COORDINATES 		= iProperties.getProperty("BOX_HEADER_COORDINATES");
				this.X_ALIGN_LEFT 					= iProperties.getProperty("X_ALIGN_LEFT");
				this.X_ALIGN_COMPANY_NAME 			= iProperties.getProperty("X_ALIGN_COMPANY_NAME");
				this.X_ALIGN_HEADER_VALUE 			= iProperties.getProperty("X_ALIGN_HEADER_VALUE");
				this.X_ALIGN_ITEM_PRICE 			= iProperties.getProperty("X_ALIGN_ITEM_PRICE");
				this.X_ALIGN_ITEM_DISC_PRICE 		= iProperties.getProperty("X_ALIGN_ITEM_DISC_PRICE");
				this.X_ALIGN_TOTAL_PRICE 		    = iProperties.getProperty("X_ALIGN_TOTAL_PRICE");
				this.Y_START_VALUE 					= iProperties.getProperty("Y_START_VALUE");
				this.X_ALIGN_COMPANY_NAME 			= iProperties.getProperty("X_ALIGN_COMPANY_NAME");
				this.ITEM_NAME_MAX_CHARACTER		= Integer.valueOf(iProperties.getProperty("ITEM_NAME_MAX_CHARACTER"));

			} catch (Exception e) {
				throw new BusinessException("Se ha producido la siguiente Exception: " + e.getLocalizedMessage());
			} finally {
				iInputStream.close();
			}

		}
	
}
