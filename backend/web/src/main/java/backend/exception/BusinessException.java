package backend.exception;

import org.springframework.http.HttpStatus;


/**
 * Un <code>BusinessException</code> es una <strong>EXCEPCION</strong>, que sirve para encpsular los 
 * casos donde el flujo normal no puede seguir adelante, o bien, que suceda un flujo realmente de excepcion.
 * <br>
 *
 * Su mision es encapsular las excepciones controladas y no controladas, en cualquiera de las distintas
 * capas de la arquitectura, y comunicarla mediante el throw de ella misma, hasta llegar al RestController
 * pertinente, donde será serializada via la clase BusinessExceptionDTO
 * <br><br>
 * Un <code>BusinessException</code> contiene:<br><br>
 * <strong>ClassName</strong>, el nombre de la clase desde el cual el flujo de excepcion comenzo.<br>
 * <strong>MethodName</strong>, el nombre del metodo que disparo la excepcion.<br>
 * <strong>ExMessage</strong>, el mensaje de excepcion, <strong>NO user-friendly</strong>, opcional, que puede provenir de una excepcion <br>
 * <strong>FriendlyMessage</strong>, el mensaje de excepcion, <strong>user-friendly</strong>, que indica que sucedio. (Por default: "Ha ocurrido una excepcion". <br>
 * <strong>StatusCode</strong>, el <code>HttpStatus Code</code> que identifica que tipo de codigo HTTP tendra que viajar al cliente (Ej. 404).<br>
 * 
 * @author genesis
 *
 */
public class BusinessException extends Exception{
	private static final long serialVersionUID = -3332292346834265371L;
	 
	public String iClassName;
	public String iMethodName;
	public String iExMessage;
	public String iFriendlyMessage;
	public HttpStatus iStatusCode;
	
    
    
	/**
	 * @param iClassName
	 * @param iMethodName
	 * @param iExMessage
	 * @param iFriendlyMessage
	 */
	public BusinessException(String pClassName, String pMethodName, String pExMessage, String pRequestUrl, HttpStatus pStatusCode) {
		super("CLASS: " + pClassName + " - METHOD: " + pMethodName +" - EX MESSAGE: " + pExMessage  );
		
		this.iClassName 	= pClassName;
		this.iMethodName 	= pMethodName;
		this.iExMessage 	= pExMessage;
		this.iFriendlyMessage 	= pRequestUrl;
		this.iStatusCode	= pStatusCode;
		
	}




	public String getClassName() {
		return iClassName;
	}


	public void setClassName(String pClassName) {
		this.iClassName = pClassName;
	}


	public String getMethodName() {
		return iMethodName;
	}


	public void setMethodName(String pMethodName) {
		this.iMethodName = pMethodName;
	}


	public String getExMessage() {
		return iExMessage;
	}


	public void setExMessage(String pExMessage) {
		this.iExMessage = pExMessage;
	}


	public String getFriendlyMessage() {
		return iFriendlyMessage;
	}


	public void setFriendlyMessage(String pFriendlyMessage) {
		this.iFriendlyMessage = pFriendlyMessage;
	}




	public HttpStatus getiStatusCode() {
		return iStatusCode;
	}




	public void setiStatusCode(HttpStatus iStatusCode) {
		this.iStatusCode = iStatusCode;
	}
}
