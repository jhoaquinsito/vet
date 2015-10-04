package backend.exception;

/**
 * Un <code>BusinessException</code> es una <strong>EXCEPCION</strong>, que sirve para encpsular los 
 * casos donde el flujo normal no puede seguir adelante, o bien, que suceda un flujo realmente de excepcion.
 * <br>
 *
 * Su mision es encapsular las excepciones controladas y no controladas, en cualquiera de las distintas
 * capas de la arquitectura, y comunicarla mediante el throw de ella misma, hasta llegar al RestController
 * pertinente, donde será serializada via la clase BusinessExceptionDTO
 * 
 * @author genesis
 *
 */
public class BusinessException extends Exception{
	
	private static final long serialVersionUID = -3332292346834265371L;
    
	
	public BusinessException(String pMessage, Exception pInnerException) {
		super(pMessage,pInnerException);
	}

	public BusinessException(String pMessage) {
		super(pMessage);
	}

}
