package backend.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Un <code>BusinessException</code> es una <strong>EXCEPCION</strong>, que
 * sirve para encapsular los casos donde el flujo normal no puede seguir
 * adelante, o bien, que suceda un flujo realmente de excepcion. <br>
 *
 * Su mision es encapsular las excepciones controladas y no controladas, en
 * cualquiera de las distintas capas de la arquitectura, y comunicarla mediante
 * el throw de ella misma, hasta llegar al RestController pertinente, donde será
 * serializada via la clase ErrorDTO.
 * 
 * @author genesis
 *
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	/**
	 * Constructor.
	 * @param pMessage mensaje de error
	 * @param pInnerException excepción que generó el error
	 */
	public BusinessException(String pMessage, Exception pInnerException) {
		super(pMessage, pInnerException);
	}
	
	/**
	 * Constructor
	 * @param pMessage mensaje de error
	 */
	public BusinessException(String pMessage) {
		super(pMessage);
	}

	/**
	 * Método que devuelve el stack trace completo de la excepción en formato de
	 * string.
	 * 
	 * @return stack trace en formato string
	 */
	public String getStackTraceString() {
		StringWriter mStackTrace = new StringWriter();

		this.printStackTrace(new PrintWriter(mStackTrace));

		return mStackTrace.toString();
	}

}
